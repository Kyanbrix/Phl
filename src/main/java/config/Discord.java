package config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kyanbrix.Main;
import net.dv8tion.jda.api.entities.WebhookClient;

import java.io.IOException;

public class Discord {

    private final static ObjectMapper mapper = new ObjectMapper();


    public static String TOKEN() {

        try {

            JsonNode node = mapper.readTree(Main.class.getClassLoader().getResourceAsStream("config.json"));
            return node.get("token").asText();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public static String url() {


        try {

            JsonNode node = mapper.readTree(Main.class.getClassLoader().getResourceAsStream("config.json"));
            return node.get("url").asText();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String username() {

        try {

            JsonNode node = mapper.readTree(Main.class.getClassLoader().getResourceAsStream("config.json"));
            return node.get("name").asText();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String password() {

        try {

            JsonNode node = mapper.readTree(Main.class.getClassLoader().getResourceAsStream("config.json"));
            return node.get("pass").asText();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String clientUpdate() {
        try {

            return mapper.readTree(Main.class.getClassLoader().getResourceAsStream("config.json")).get("clientUpdate").asText();

        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String clientDelete() {
        try {

            return mapper.readTree(Main.class.getClassLoader().getResourceAsStream("config.json")).get("clientDelete").asText();

        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }




}
