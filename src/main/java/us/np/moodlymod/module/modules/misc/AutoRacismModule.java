package us.np.moodlymod.module.modules.misc;

import net.minecraft.network.play.client.CPacketChatMessage;
import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.OptionDouble;

import java.awt.*;

public class AutoRacismModule extends Module {
    public static final OptionDouble delay = new OptionDouble("Delay", 1D, 0.5D, 20D);

    public AutoRacismModule() {
        super("AutoRacism", new String[] {""}, "NONE", Color.PINK, ModuleType.MISC);
        addOption(delay);
        endOption();
    }

    Thread thread;

    @Override
    public void onEnable() {
        super.onEnable();
        thread = new Thread(() -> {
            try {
                while(true) {
                    Thread.sleep((int) (delay.getValue().doubleValue() * 1000));
                    if (mc.player != null)
                        mc.player.connection.sendPacket(new CPacketChatMessage("I hate black people ₿ " + MoodlyMod.NAME));
                }
            } catch(Exception e) { e.printStackTrace(); }
        });

        thread.start();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        thread.stop();
        if(mc.player != null) mc.player.connection.sendPacket(new CPacketChatMessage("I still hate black people ₿ " + MoodlyMod.NAME));
    }
}