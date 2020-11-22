package us.np.moodlymod.module.modules.misc;

import java.awt.Color;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.Wrapper;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.OptionBoolean;

public class DiscordRPCModule extends Module {
    public static final OptionBoolean showDefault = new OptionBoolean("Show Default", true);
    public static final OptionBoolean showDevelopers = new OptionBoolean("Developers Info", false);
    public static final OptionBoolean showPlayerInfo = new OptionBoolean("Player Info", true);

    DiscordRPC discordRPC = DiscordRPC.INSTANCE;
    DiscordRichPresence presence;
    Thread[] thread = new Thread[10];

    public DiscordRPCModule() {
        super("DiscordRPC", new String[]{""}, "NONE", Color.WHITE, ModuleType.MISC);
        addOption(showDefault);
        addOption(showDevelopers);
        addOption(showPlayerInfo);
        endOption();
    }

    public String[] updatePresence() {
        String[] presence = {"", ""};
        /* Presence */
        if(showDefault.getValue()) presence[0] = "Using " + MoodlyMod.NAME + " " + MoodlyMod.VERSION;
        else if(showDevelopers.getValue()) presence[0] = "Developed";
        else if(showPlayerInfo.getValue()) presence[0] = "Player Info";
        /* State */
        if(showDefault.getValue()) presence[1] = "Client For Anarchy";
        if(showDevelopers.getValue()) presence[1] = "by CGrego3211 and PK2_Stimpy";
        else if(showPlayerInfo.getValue())
            if(DiscordRPCModule.mc.player != null)
                presence[1] = "My health is " + Wrapper.getMC().player.getHealth();

        return presence;
    }

    @Override
    public void onEnable() {
        super.onEnable();

        String applicationId = "770712404031701002";
        String steamId = "";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        discordRPC.Discord_Initialize(applicationId, handlers, true, steamId);
        presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        discordRPC.Discord_UpdatePresence(presence);

        thread[0] = new Thread(() -> {
            discordRPC.Discord_RunCallbacks();
            String[] strings = updatePresence();
            presence.details = strings[0];
            presence.state = strings[1];
            discordRPC.Discord_UpdatePresence(presence);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {}
        }, "RPC-Callback-Handler");

        thread[0].start();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        for(Thread thread : this.thread)
            if(thread != null)
                thread.stop();
    }
}