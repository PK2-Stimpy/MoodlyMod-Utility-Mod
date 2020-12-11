package us.np.moodlymod.module.modules.misc;

import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.BetterMode;
import us.np.moodlymod.module.option.OptionBetterMode;
import us.np.moodlymod.util.Timer;

import java.awt.*;

public class AntiAFKModule extends Module {
    public static OptionBetterMode mode = new OptionBetterMode("Mode", 0, BetterMode.construct("2B2T", "L2X9"));

    private Timer timer;
    public AntiAFKModule() {
        super("AntiAFK", null, "NONE", Color.YELLOW, ModuleType.MISC);
        addOption(mode);
        endOption();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        timer = new Timer();
    }

    @EventHandler
    private Listener<TickEvent.ClientTickEvent> clientTickEventListener = new Listener<>(event -> {
        if(!timer.passed(30000)) return;
        timer.reset();

        if(mode.getMode() == 0)
            mc.player.connection.sendPacket(new CPacketChatMessage("/stats")); /* 2B2T */
        else if(mode.getMode() == 1)
            mc.player.connection.sendPacket(new CPacketChatMessage("/help")); /* L2X9 */
    });

    @Override
    public String getMeta() {
        return mode.getDisplayName();
    }
}