package events.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.jetbrains.annotations.NotNull;
import utilities.Config;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public abstract class Command {

    public abstract void execute(MessageReceivedEvent event);

    public abstract String getName();

    public String[] getAliases(){
        return new String[0];
    }

    protected Button add_button(ButtonStyle style, String id, String label){
        return Button.of(style,id,label);
    }


    protected void sendMessage(@NotNull MessageChannelUnion channelUnion, @NotNull String message){
        channelUnion.sendMessage(message).queue();
    }

    protected void delete_after(@NotNull MessageChannelUnion channelUnion, String message, long delay, TimeUnit timeUnit){
        channelUnion.sendMessage(message).delay(delay,timeUnit).flatMap(Message::delete).queue();
    }

    protected void send_embed(MessageChannelUnion channelUnion, EmbedBuilder builder){
        channelUnion.sendMessageEmbeds(builder.setColor(Color.decode(Config.default_color)).build()).queue();
    }

    protected void addReaction(Message message){
        message.addReaction(Emoji.fromFormatted("<:Ok:1018795500062920725>")).queue();
    }


}
