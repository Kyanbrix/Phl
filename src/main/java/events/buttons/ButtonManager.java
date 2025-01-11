package events.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ButtonManager extends ListenerAdapter {
    private final Map<String, IButton> buttons = new HashMap<>();


    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        final String buttonId = event.getButton().getId();

        IButton button = buttons.get(buttonId);

        if (button == null) {
            event.editComponents(ActionRow.of(event.getMessage().getButtons())).queue();
            return;
        }

        button.execute(event);
    }

    public void addButtons(IButton ... iButton) {

        for (IButton b : iButton) {

            if (buttons.containsKey(b.id())) throw new RuntimeException("Duplicate Button ID");

            this.buttons.put(b.id(),b);
        }


    }
}
