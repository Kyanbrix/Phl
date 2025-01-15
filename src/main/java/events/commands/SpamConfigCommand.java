package events.commands;

import com.github.kyanbrix.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utilities.Constant;

public class SpamConfigCommand extends Command{
    @Override
    public void execute(MessageReceivedEvent event) {


        String configMsg = removePrefix(event.getMessage().getContentRaw());

        if (configMsg.equalsIgnoreCase("e") || configMsg.equalsIgnoreCase("enable")) {

            if (!Main.getInstance().getSpying()) {
                Main.getInstance().setSpying(true);
                sendMessage(event.getChannel(),"Anti-Spamming is Enabled!");
            }else sendMessage(event.getChannel(),"Anti-Spamming Configuration is already Enabled!");



        } else if (configMsg.equalsIgnoreCase("d") || configMsg.equalsIgnoreCase("disable")) {
            if (!Main.getInstance().getSpying()) {
                Main.getInstance().setSpying(false);
                sendMessage(event.getChannel(),"Anti-Spamming is Disabled!");
            }else sendMessage(event.getChannel(),"Anti-Spamming Configuration is already Disabled!");
        }


    }

    @Override
    public String getName() {
        return "antispam";
    }

    private String removePrefix(String message) {

        return message.substring(Constant.prefix.length() + getName().length()).strip();

    }
}
