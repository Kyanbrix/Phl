package events;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface IButton {

    void execute(ButtonInteractionEvent event);

    String id();

}
