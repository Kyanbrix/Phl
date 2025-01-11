package listeners;

import database.SQLBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.ErrorResponse;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class Counting extends ListenerAdapter {
    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
        if (!event.isFromGuild()) return;

        long msgID = event.getMessageIdLong();

        var ps = new SQLBuilder("SELECT * FROM phl_counting");

        try (var set = ps.executeQuery()){

            if (set.next()) {

                long current_count = set.getLong("count_number");
                long id = set.getLong("user_name");

                if (msgID == set.getLong("message_id")) {
                    event.getChannel().sendMessageFormat("%s has deleted their number **%d**, FUCKING moron! \n``` The next number is %d ```", User.fromId(id).getAsMention(),current_count - 1,current_count).queue();
                }
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (!event.isFromGuild()) return;

        if (event.getMessage().getChannelIdLong() != 1308756506661097535L) return;

        if (event.getAuthor().isBot()) return;


        var ps = new SQLBuilder("SELECT count_number FROM phl_counting");

        try (var set = ps.executeQuery()) {

            if (set.next()) {

                try {
                    long count = set.getLong("count_number");

                    if (count - Integer.parseInt(event.getMessage().getContentRaw()) == 1)
                    {
                        var update = new SQLBuilder("UPDATE phl_counting SET count_number = ?, message_id = ?, user_name = ?")
                                .addParameters(count + 1, event.getMessage().getIdLong(),event.getAuthor().getIdLong());
                        update.executeUpdate();

                        event.getMessage().addReaction(Emoji.fromFormatted("<:5Head:947546130667950110>")).queue();
                        event.getMessage().removeReaction(Emoji.fromFormatted("<:5Head:947546130667950110>")).queueAfter(10, TimeUnit.SECONDS,null, new ErrorHandler().handle(ErrorResponse.UNKNOWN_MESSAGE, e -> System.out.println(e.getMessage())));

                    }else {
                        if (count == 2)
                        {

                            event.getMessage().reply(event.getAuthor().getAsMention()+", retard can't count. The next number is **1** braindead").queue();

                        }else
                        {
                            var update = new SQLBuilder("UPDATE phl_counting SET count_number = ?, message_id = ?, user_name = ?")
                                    .addParameters(2, null,null);
                            update.executeUpdate();
                            event.getMessage().addReaction(Emoji.fromFormatted("<:Pepega:1321089327568126052>")).queue();
                            event.getMessage().reply(event.getAuthor().getAsMention()+" **Wrong Number!.** Retard cannot count!!\n ``` Count has been reset back to 1 ```").queue();
                        }

                    }
                }catch (NumberFormatException ignored) {
                    event.getMessage().delete().queue();
                }
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
