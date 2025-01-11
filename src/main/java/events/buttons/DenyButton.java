package events.buttons;

import database.SQLBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DenyButton implements IButton{
    private static final Logger log = LoggerFactory.getLogger(DenyButton.class);

    @Override
    public void execute(ButtonInteractionEvent event) {


        var ps = new SQLBuilder("SELECT user_id FROM process WHERE msg_id = ?")
                .addParameter(event.getMessageIdLong());

        try (var resultSet = ps.executeQuery()) {

            if (resultSet.next()) {
                TextInput input = TextInput.create("denyInput","Reason", TextInputStyle.PARAGRAPH).setRequired(true).setMinLength(3).setMaxLength(100).build();

                Modal modal = Modal.create("denyModal","Denied Form").addActionRow(input).build();

                event.replyModal(modal).queue();
            }else event.editComponents(ActionRow.of(event.getMessage().getButtons())).queue();



        }catch (SQLException e) {
            log.error(e.getMessage(),e.fillInStackTrace());
        }

    }

    @Override
    public String id() {
        return "deny";
    }
}
