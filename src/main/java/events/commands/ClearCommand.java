package events.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utilities.Constant;
import config.GuildConfig;

import java.util.concurrent.TimeUnit;

public class ClearCommand extends Command{
    @Override
    public void execute(MessageReceivedEvent event) {

        Member member = event.getMember();

        if (member == null) return;

        if (!GuildConfig.INSTANCE.isStaff(member) || !event.isFromGuild()) return;

        final String content = remove_prefix(event.getMessage().getContentRaw());
        MessageChannel channel = event.getChannel();

        if (content.isEmpty()) {
            channel.getIterableHistory().takeWhileAsync(message -> true).thenAcceptAsync(channel::purgeMessages).whenComplete((unused, throwable) -> {
                channel.sendMessage(channel.getAsMention()+" messages has been successfully deleted").delay(30, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            });

        }else {
            try {

                int limit = Integer.parseInt(content);

                channel.getIterableHistory().takeAsync(limit).thenAcceptAsync(channel::purgeMessages).whenComplete((unused, throwable) -> {

                    channel.sendMessage(limit+" messages has been successfully deleted!").delay(30,TimeUnit.SECONDS).flatMap(Message::delete).queue();
                });
            }catch (NumberFormatException e) {
                event.getChannel().sendMessage("Wrong input! Try Again").queue();
            }
        }
    }

    @Override
    public String getName() {
        return "clear";
    }
    private String remove_prefix(String message) {

        String content = message.substring(Constant.prefix.length() + getName().length());

        if (content.startsWith(" ")) content = content.substring(1);

        return content;

    }

}
