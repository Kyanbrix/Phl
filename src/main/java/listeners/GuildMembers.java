package listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.dv8tion.jda.api.utils.TimeFormat;
import org.jetbrains.annotations.NotNull;
import utilities.Constant;

import java.awt.*;

public class GuildMembers extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

        final long id = event.getUser().getIdLong();
        Guild guild = event.getGuild();
        TextChannel channel = guild.getTextChannelById(939972594206146643L);

        if (channel == null) return;

        guild.retrieveMemberById(id).queue(member -> {

            MessageEmbed embed = new EmbedBuilder()
                    .setAuthor(member.getEffectiveName(),null,member.getEffectiveAvatarUrl())
                    .setDescription(member.getAsMention() + " has joined the server")
                    .setColor(Color.decode(Constant.default_color))
                    .addField("Name",member.getUser().getName(),true).addField("Joined", TimeFormat.RELATIVE.format(member.getTimeJoined()),true).addBlankField(true)
                    .addField("Creation",String.format("%s (%s)",TimeFormat.DATE_TIME_SHORT.format(member.getTimeCreated()),TimeFormat.RELATIVE.format(member.getTimeCreated())), true)
                    .setFooter("Server has now "+guild.getMemberCount()+" members",guild.getIconUrl())
                    .build();


            channel.sendMessageEmbeds(embed).queue();


        },new ErrorHandler().handle(ErrorResponse.UNKNOWN_MEMBER,e -> channel.sendMessage("Member not Found!").queue()));
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        final long id = event.getUser().getIdLong();

        JDA jda = event.getJDA();

        TextChannel channel = event.getGuild().getTextChannelById(1201869426199109662L);

        if (channel == null) return;

        jda.retrieveUserById(id).queue(user -> {

            MessageEmbed embed = new EmbedBuilder()
                    .setAuthor(user.getEffectiveName(),null,user.getEffectiveAvatarUrl())
                    .setDescription(user.getAsMention() +" has left the server")
                    .setThumbnail(user.getEffectiveAvatarUrl())
                    .setColor(Color.RED)
                    .addField("Name",user.getName(),true).addField("Left", TimeFormat.RELATIVE.now().toString(),true)
                    .setFooter("Server has now "+event.getGuild().getMemberCount()+" members",event.getGuild().getIconUrl())
                    .build();

            channel.sendMessageEmbeds(embed).queue();

        },new ErrorHandler().handle(ErrorResponse.UNKNOWN_USER, e -> channel.sendMessage("Unknown User").queue()));
    }
}
