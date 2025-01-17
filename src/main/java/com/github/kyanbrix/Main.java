package com.github.kyanbrix;

import events.buttons.AcceptButton;
import events.buttons.DenyButton;
import listeners.Counting;
import listeners.GuildMembers;
import config.Discord;
import database.ConnectionFromPool;
import events.buttons.ButtonManager;
import events.commands.Registry;
import listeners.ServerMessages;
import listeners.SpamListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Main {


    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static Main instance;
    private final ConnectionFromPool connectionFromPool;
    private final JDA jda;
    private Boolean isSpying = false;

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }


    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    public JDA getJda() {
        return jda;
    }
    public ConnectionFromPool getConnectionFromPool() {
        return connectionFromPool;
    }

    public static Main getInstance() {
        return instance;
    }

    public Boolean getSpying() {
        return isSpying;
    }

    public void setSpying(Boolean spying) {
        isSpying = spying;
    }

    private EnumSet<GatewayIntent> intents() {
        return EnumSet.of(GatewayIntent.GUILD_MEMBERS,GatewayIntent.GUILD_MESSAGES,GatewayIntent.MESSAGE_CONTENT,GatewayIntent.DIRECT_MESSAGES);
    }


    public Main() {
        instance = this;
        connectionFromPool = new ConnectionFromPool();
        shutdown();

        var buttonManager = new ButtonManager();
        buttonManager.addButtons(new DenyButton(), new AcceptButton());

        jda = JDABuilder.createLight(Discord.TOKEN(),intents())
                .setMemberCachePolicy(MemberCachePolicy.NONE.or(member -> !member.getUser().isBot()))
                .setChunkingFilter(ChunkingFilter.NONE)
                .setAutoReconnect(true)
                .disableCache(EnumSet.allOf(CacheFlag.class))
                .setEnableShutdownHook(false)
                .addEventListeners(buttonManager)
                .addEventListeners(new SpamListener(),new Registry(), new Counting(), new Counting(), new GuildMembers(), new ServerMessages())
                .build();


    }

    public static void main(String[] args) {

        new Main();

    }

    private void shutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

            getJda().shutdown();
            Main.getInstance().getConnectionFromPool().closeConnection();

            log.info("JDA is shutdown!");


        }));
    }
}