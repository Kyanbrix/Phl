package events.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ButtonManager extends ListenerAdapter {
    private final Map<String, IButton> buttons = new HashMap<>();


    public ButtonManager() {

        this.addButtons(new AcceptButton());

    }


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

    public void addButtons(IButton iButton)
    {
        if (iButton.id().contains(" ")) throw new IllegalArgumentException("Button id must not have spaces");

        if (buttons.containsKey(iButton.id())) throw new IllegalArgumentException("Duplicate button id");

        this.buttons.put(iButton.id(),iButton);


    }
}
