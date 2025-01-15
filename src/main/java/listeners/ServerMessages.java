package listeners;

import com.github.kyanbrix.Main;
import config.Discord;
import data.MessageData;
import database.SQLBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilities.Constant;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class GuildMessages extends ListenerAdapter {
    private static final Logger log = LoggerFactory.getLogger(GuildMessages.class);

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event) {

        if (!event.isFromGuild()) return;

        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();


        var ps = new SQLBuilder("SELECT * FROM messages WHERE msg_id = ?")
                .addParameter(message.getIdLong());

        try (ResultSet set = ps.executeQuery()) {

            if (set.next()) {

                MessageEmbed embed = new EmbedBuilder()
                        .setAuthor(message.getAuthor().getName(),null,message.getAuthor().getEffectiveAvatarUrl())
                        .setColor(Color.decode(Constant.default_color))
                        .addField("New Message",message.getContentRaw(),false)
                        .addField("Old Message",set.getString("msg_cnt"),false)
                        .setDescription(message.getAuthor().getAsMention()+" updated their message [Jump to Message]("+message.getJumpUrl()+")")
                        .setTimestamp(message.getTimeEdited())
                        .build();

                Constant.getClient(Discord.clientUpdate()).sendMessageEmbeds(embed).queue();
            }




        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

        finally {
            ps.close();
        }



        var update = new SQLBuilder("UPDATE messages SET msg_cnt = ? WHERE msg_id = ?")
                .addParameters(message.getContentRaw(),message.getIdLong());

        try {
            update.executeUpdate();
            update.close();
        }catch (SQLException e) {
            log.error(e.getMessage());
        }






    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {

        if (!event.isFromGuild()) return;

        long msgID = event.getMessageIdLong();


        var ps = new SQLBuilder("SELECT * FROM messages WHERE msg_id = ?")
                .addParameter(msgID);

        try (ResultSet set = ps.executeQuery()) {

            if (set.next()) {

                String content = set.getString("msg_cnt");

                Member member = event.getGuild().getMemberById(set.getLong("userid"));

                if (member == null) {
                    log.error("Cannot retrieve this given member Id!");
                    return;
                }



                MessageEmbed embed = new EmbedBuilder()
                        .setAuthor(member.getUser().getName(),null,member.getEffectiveAvatarUrl())
                        .setDescription(member.getAsMention()+" deleted a message in "+event.getChannel().getAsMention()+" \n\n"+content)
                        .setTimestamp(OffsetDateTime.now(ZoneId.of("Asia/Manila")))
                        .setColor(Color.decode(Constant.default_color))
                        .build();


                Constant.getClient(Discord.clientDelete()).sendMessageEmbeds(embed).queue();

                var del = new SQLBuilder("DELETE FROM messages WHERE msg_id = ?")
                        .addParameter(msgID);

                int row = del.executeUpdate();

                if (row != 0) {
                    log.info("Successfully delete a message data");
                }

                del.close();

            }

        }catch (SQLException e) {
            log.error(e.getMessage());

        }finally {
            ps.close();
        }





    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (!event.isFromGuild()) return;

        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();

        if (message.getContentRaw().isEmpty()) return;


        try (var connection = Main.getInstance().getConnectionFromPool().getConnection()){

            MessageData.insertUserMessages(connection,message.getContentRaw(),message.getIdLong(),message.getAuthor().getIdLong(),message.getTimeCreated());
            
        }catch (SQLException e) {
            log.error("Cannot insert msg data",e);
        }



        var ps = new SQLBuilder("INSERT INTO messages (userid, msg_id, msg_cnt, createdtime) VALUES (?,?,?,?)")
                .addParameters(message.getAuthor().getIdLong(),message.getIdLong(),message.getContentRaw(), LocalDate.from(message.getTimeCreated()));

        try {

         int row = ps.executeUpdate();

         if (row == 0) {
             log.error("Something went wrong when inserting a message data to the database messages table");
         }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
