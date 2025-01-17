package listeners;

import com.github.kyanbrix.Main;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import config.GuildConfig;

import java.util.concurrent.TimeUnit;

public class SpamListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (!Main.getInstance().getSpying()) return;

        if (event.getAuthor().getIdLong() == GuildConfig.OWNER_ID || event.getAuthor().isBot() || !event.isFromGuild()) return;

        MessageChannel channel = event.getChannel();

        channel.getIterableHistory().takeWhileAsync(10, message -> message.getAuthor().equals(event.getAuthor())).whenComplete((messages, throwable) -> {

            if (throwable != null) {
                channel.sendMessage(throwable.getMessage()).queue();
                return;
            }

            int spam_count = messages.stream().filter(message -> (event.getMessage().getTimeCreated().toEpochSecond() - message.getTimeCreated().toEpochSecond() < 6)).toList().size();


            if (spam_count > 2) {
                channel.sendMessage("Stop Spamming "+event.getAuthor().getAsMention()).delay(10, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            }

        });
    }
}
