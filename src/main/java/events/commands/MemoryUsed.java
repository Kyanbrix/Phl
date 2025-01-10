package events.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MemoryUsed extends Command{
    @Override
    public void execute(MessageReceivedEvent event) {

        Runtime runtime = Runtime.getRuntime();


        runtime.gc();

        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;

        long mbUsed = usedMemory / (1024 * 1024);

        event.getChannel().sendMessageFormat("My total memory used: **%d MB**",mbUsed).queue();
    }

    @Override
    public String getName() {
        return "memory";
    }
}
