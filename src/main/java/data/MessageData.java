package data;

import config.Discord;
import database.SQLBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilities.Constant;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class MessageData {

    private static final Logger log = LoggerFactory.getLogger(MessageData.class);

    public static void insertUserMessages(Connection connection, String content, long msgID, long userID, OffsetDateTime msgCreatedTime) {

        var ps = "INSERT INTO messages (userid, msg_id, msg_cnt, createdtime) VALUES (?,?,?,?)";

        var sql = new SQLBuilder(ps)
                .addConnection(connection)
                .addParameters(userID,msgID,content,msgCreatedTime);

        try {
            sql.executeUpdate();
        }catch (SQLException e) {
            log.error("Cannot add message data!",e);
        }


    }

    public static void deletedMessageEvent(Connection connection, Guild guild, MessageChannel channel, long msgID) {

        var sql = new SQLBuilder("SELECT * FROM messages WHERE msg_id = ?")
                .addConnection(connection)
                .addParameter(msgID);

        try (ResultSet set = sql.executeQuery()){

            if (set.next()) {

                Member member = guild.getMemberById(set.getLong("userid"));

                MessageEmbed embed = new EmbedBuilder()
                        .setAuthor(member.getUser().getName(),null,member.getEffectiveAvatarUrl())
                        .setDescription(member.getAsMention()+" deleted a message in "+channel.getAsMention()+" \n\n"+set.getString("msg_cnt"))
                        .setTimestamp(OffsetDateTime.now(ZoneId.of("Asia/Manila")))
                        .setColor(Color.decode(Constant.default_color))
                        .build();

                Constant.getClient(Discord.clientDelete()).sendMessageEmbeds(embed).queue();

            }


        }catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    public static int updateMessage(Message message) {

        var sql = new SQLBuilder("UPDATE messages SET msg_cnt = ? WHERE msg_id = ?")
                .addParameters();

        try {

            return sql.executeUpdate();

        }catch (SQLException e) {
            throw  new RuntimeException(e);
        }

    }


    public static void messageUpdateEvent(Connection connection, Message message) {

        var sql = new SQLBuilder("SELECT * FROM messages WHERE msg_id = ?")
                .addConnection(connection)
                .addParameters(message.getIdLong());

        try (ResultSet set = sql.executeQuery()) {

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
            log.error(e.getMessage());
        }



    }


}
