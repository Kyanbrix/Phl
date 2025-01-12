package events.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;
import utilities.Constant;

import java.awt.*;
import java.util.Objects;

public class AvatarCommand extends Command{
    @Override
    public void execute(MessageReceivedEvent event) {

        Message message = event.getMessage();


        EmbedBuilder embed = new EmbedBuilder();

        if (message.getType().equals(MessageType.INLINE_REPLY)){
            User user = Objects.requireNonNull(message.getReferencedMessage()).getAuthor();


            embed.setAuthor(user.getEffectiveName()+"'s avatar",null,event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                    .setColor(Color.decode(Constant.default_color))
                    .setImage(user.getEffectiveAvatar().getUrl(600));

            event.getChannel().sendMessageEmbeds(embed.build()).queue();


            return;
        }

        final String command = remove_prefix(message.getContentRaw());

        if (command.isEmpty()) {

            User user = event.getAuthor();
            embed.setAuthor(user.getEffectiveName()+"'s avatar",null,event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                    .setColor(Color.decode(Constant.default_color))
                    .setImage(user.getEffectiveAvatar().getUrl(600));

            event.getChannel().sendMessageEmbeds(embed.build()).queue();

        }else {
            event.getJDA().retrieveUserById(command).queue(user1 -> {

                embed.setAuthor(user1.getEffectiveName()+"'s avatar",null,event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                        .setColor(Color.decode(Constant.default_color))
                        .setImage(user1.getEffectiveAvatar().getUrl(600));

                event.getChannel().sendMessageEmbeds(embed.build()).queue();

            },new ErrorHandler().handle(ErrorResponse.UNKNOWN_USER, e -> event.getChannel().sendMessage("User not Found!").queue()));



        }
    }

    @Override
    public String[] getAliases() {
        return new String[]{"avatar","profile","pf","dp"};
    }

    @Override
    public String getName() {
        return "av";
    }

    private String remove_prefix(String message) {

        return message.substring(Constant.prefix.length() + getName().length()).strip();

    }
}
