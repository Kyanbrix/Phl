package events.buttons;

import database.SQLBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilities.Config;
import utilities.GuildConfig;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class AcceptButton implements IButton {
    private static final Logger log = LoggerFactory.getLogger(AcceptButton.class);

    @Override
    public void execute(ButtonInteractionEvent event) {


        var ps = new SQLBuilder("SELECT * FROM process WHERE msg_id = ?")
                .addParameter(event.getMessageIdLong());

        Guild guild = event.getGuild();

        try (ResultSet set = ps.executeQuery()){

            if (set.next()) {
                long id = set.getLong("user_id");
                Member member = guild.getMemberById(id);

                String ign = set.getString("aqw_ign");

                if (member != null) {
                    guild.addRoleToMember(member,guild.getRoleById(GuildConfig.ALLOWLISTED_ROLE)).flatMap(unused -> guild.modifyNickname(member,ign)).queue();
                }

                MessageEmbed embed = new EmbedBuilder()
                        .setAuthor(member.getUser().getName(),null,member.getEffectiveAvatarUrl())
                        .setColor(Color.decode(Config.default_color))
                        .addField("User",member.getAsMention(),false)
                        .addField("AQW IGN","https://account.aq.com/CharPage?id="+(ign.contains(" ") ? ign.replace(" ","%20") : ign),false)
                        .addField("Status","Approved",false)
                        .setTimestamp(Instant.now())
                        .build();

                event.editComponents(ActionRow.of(Button.of(ButtonStyle.SUCCESS,"done","Approved").asDisabled()))
                        .flatMap(interactionHook -> interactionHook.editOriginalEmbeds(embed))
                        .queue();
            }



        }catch (SQLException e) {
            log.error(e.getMessage(),e.fillInStackTrace());
        }




    }

    @Override
    public String id() {
        return "accept";
    }
}
