package listeners;

import com.github.kyanbrix.Main;
import data.MessageData;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class ServerMessages extends ListenerAdapter {
    private static final Logger log = LoggerFactory.getLogger(ServerMessages.class);

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event) {

        if (!event.isFromGuild()) return;

        if (event.getAuthor().isBot()) return;

        if (event.getMessage().isWebhookMessage()) return;

        try (Connection connection = Main.getInstance().getConnectionFromPool().getConnection()) {

            MessageData.messageUpdateEvent(connection,event.getMessage());

            MessageData.updateMessage(event.getMessage());

        }catch (SQLException e) {
            log.error("Error On Message Update",e);
        }

    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {

        if (!event.isFromGuild()) return;

        long msgID = event.getMessageIdLong();

        try (Connection connection = Main.getInstance().getConnectionFromPool().getConnection()) {

            MessageData.deletedMessageEvent(connection,event.getGuild(),event.getChannel(),msgID);

        }catch (SQLException e) {
            e.fillInStackTrace();
        }



    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (!event.isFromGuild()) return;

        if (event.getAuthor().isBot()) return;

        if (event.getMessage().isWebhookMessage()) return;

        Message message = event.getMessage();


        try (var connection = Main.getInstance().getConnectionFromPool().getConnection()) {

            MessageData.insertUserMessages(connection,message.getContentRaw(),message.getIdLong(),message.getAuthor().getIdLong(),message.getTimeCreated());

        }catch (SQLException e) {
            log.error("Cannot insert msg data",e);
        }

    }
}
