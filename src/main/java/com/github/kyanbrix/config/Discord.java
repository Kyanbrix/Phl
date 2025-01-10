package com.github.kyanbrix.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kyanbrix.Main;

import java.io.IOException;
import java.io.InputStream;

public class Discord {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String TOKEN() {

        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("config.json")) {

            JsonNode jsonNode = objectMapper.readTree(inputStream);

            return jsonNode.get("discord_token").asText();

        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String URL() {

        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("config.json")) {

            JsonNode jsonNode = objectMapper.readTree(inputStream);

            return jsonNode.get("url").asText();

        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String username() {

        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("config.json")) {

            JsonNode jsonNode = objectMapper.readTree(inputStream);

            return jsonNode.get("username").asText();

        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String password() {

        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("config.json")) {

            JsonNode jsonNode = objectMapper.readTree(inputStream);

            return jsonNode.get("password").asText();

        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }




}
