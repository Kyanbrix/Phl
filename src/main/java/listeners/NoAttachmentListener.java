package listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import utilities.GuildConfig;

public class NoAttachmentListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.isFromGuild()) return;

        if (GuildConfig.INSTANCE.isOwner(event.getAuthor().getIdLong())) return;

        if (GuildConfig.INSTANCE.getBLOCKED_CHANNELS().contains(event.getMessage().getChannelIdLong())) {

            if (event.getMessage().getAttachments().isEmpty()) {
                event.getMessage().delete().queue();
                return;
            }

            for (Message.Attachment attachment : event.getMessage().getAttachments()) {
                if (attachment.isSpoiler()) event.getMessage().delete().queue();
            }

        }
    }
}
