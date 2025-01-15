package utilities;

import com.github.kyanbrix.Main;
import net.dv8tion.jda.api.entities.WebhookClient;

public class Constant {

    public static final long ownerID = 683613536823279794L;
    public static final String prefix = "!";
    public static final String default_color = "#6495ED";


    public static WebhookClient<?> getClient(String url) {

        return WebhookClient.createClient(Main.getInstance().getJda(), url);

    }


}
