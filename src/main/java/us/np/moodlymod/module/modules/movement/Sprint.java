package us.np.moodlymod.module.modules.movement;

import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;

import java.awt.*;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", new String[] {""}, "NONE", Color.RED, ModuleType.MOVEMENT);
        endOption();
    }

    @EventHandler
    private Listener<TickEvent.ClientTickEvent> eventListener = new Listener<>(event -> {
        if(Sprint.mc.player == null) return;

        if (!Sprint.mc.player.collidedHorizontally && Sprint.mc.player.moveForward > 0.0f) {
            Sprint.mc.player.setSprinting(true);
        }
        else {
            Sprint.mc.player.setSprinting(false);
        }
    });
}