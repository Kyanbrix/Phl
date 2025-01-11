package utilities;

import io.github.cdimascio.dotenv.Dotenv;

public class Discord {

    private static final Dotenv dotenv = Dotenv.
            configure()
            .directory("src/main/java/assets")
            .filename(".env")
            .load();

    public static String TOKEN() {

        System.out.println(dotenv.get("token"));
        return dotenv.get("token");
    }

    public static String URL() {

        return dotenv.get("url");

    }

    public static String username() {

        return dotenv.get("username");

    }

    public static String password() {

        return dotenv.get("password");

    }




}
