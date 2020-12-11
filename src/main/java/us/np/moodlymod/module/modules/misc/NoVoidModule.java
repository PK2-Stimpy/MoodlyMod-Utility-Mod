package us.np.moodlymod.module.modules.misc;

import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import us.np.moodlymod.event.custom.entityplayersp.PlayerUpdateEvent;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.OptionDouble;

import java.awt.*;

public class NoVoidModule extends Module {
    public static final OptionDouble height = new OptionDouble("Height", 3D, 0D, 256D);

    public NoVoidModule() {
        super("NoVoid", null, "NONE", Color.BLUE, ModuleType.MISC);
        addOption(height);
        endOption();
    }

    @EventHandler
    private Listener<PlayerUpdateEvent> playerUpdateEventListener = new Listener<>(event -> {
        if (NoVoidModule.mc.world != null) {
            if (NoVoidModule.mc.player.noClip || NoVoidModule.mc.player.posY > this.height.getValue()) return;

            final RayTraceResult trace = NoVoidModule.mc.world.rayTraceBlocks(NoVoidModule.mc.player.getPositionVector(), new Vec3d(NoVoidModule.mc.player.posX, 0.0, NoVoidModule.mc.player.posZ), false, false, false);
            if (trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK) return;

            NoVoidModule.mc.player.setVelocity(0.0, 0.0, 0.0);
        }
    });

    @Override
    public String getMeta() {
        return height.getValue().toString();
    }
}