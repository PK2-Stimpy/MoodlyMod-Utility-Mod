package us.np.moodlymod.module.modules.movement;

import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;

import java.awt.*;

public class SprintModule extends Module {
    public SprintModule() {
        super("Sprint", new String[] {""}, "NONE", Color.RED, ModuleType.MOVEMENT);
        endOption();
    }

    @EventHandler
    private Listener<TickEvent.ClientTickEvent> eventListener = new Listener<>(event -> {
        if(SprintModule.mc.player == null) return;

        if (!SprintModule.mc.player.collidedHorizontally && SprintModule.mc.player.moveForward > 0.0f) {
            SprintModule.mc.player.setSprinting(true);
        }
        else {
            SprintModule.mc.player.setSprinting(false);
        }
    });
}