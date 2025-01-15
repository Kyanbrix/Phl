package events.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilities.Constant;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Registry extends ListenerAdapter {

    private static final Logger log = LoggerFactory.getLogger(Registry.class);
    private final Set<Command> commands = ConcurrentHashMap.newKeySet();

    public Registry() {
        this.addCommands(new MemoryUsed());
        this.addCommands(new ClearCommand());
        this.addCommands(new BannerCommand());
        this.addCommands(new AvatarCommand());
        this.addCommands(new SpamConfigCommand());
    }


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (event.getAuthor().isBot() || !event.isFromGuild()) return;

        final String prefix = Constant.prefix;
        String message = event.getMessage().getContentRaw().toLowerCase();

        if (event.getMessage().getContentRaw().startsWith(prefix)) {

            for (final Command c : commands) {
                if (message.startsWith(prefix) && message.startsWith(c.getName(),prefix.length())) {
                    try {
                        c.execute(event);
                    }catch (Exception e){
                        log.error(e.getMessage(), e.fillInStackTrace());
                    }
                    return;
                }else {
                    for (String alias: c.getAliases()){
                        if (message.startsWith(prefix) && message.startsWith(alias,prefix.length())) {
                            try {
                                c.execute(event);
                            }catch (Exception e){
                                log.error(e.getMessage(), e.fillInStackTrace());
                            }
                            return;
                        }
                    }

                }



            }

        }



    }

    public void addCommands(final Command command){

        if (command.getName().endsWith(" ") || command.getName().startsWith(" ")) throw new IllegalArgumentException("Name must not have spaces!");

        if (this.commands.stream().map(Command::getName).anyMatch(c -> command.getName().equalsIgnoreCase(c))) throw new IllegalArgumentException("This command is already present!");

        this.commands.add(command);
    }
}
