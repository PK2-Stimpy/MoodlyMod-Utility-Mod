package us.np.moodlymod.module.modules.combat;

import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import us.np.moodlymod.event.custom.entityplayer.PlayerApplyCollisionEvent;
import us.np.moodlymod.event.custom.entityplayer.PlayerPushedByWaterEvent;
import us.np.moodlymod.event.custom.entityplayersp.PlayerPushOutOfBlocksEvent;
import us.np.moodlymod.event.custom.networkmanager.NetworkPacketEvent;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.OptionBoolean;
import us.np.moodlymod.module.option.OptionDouble;

import java.awt.*;

public class VelocityModule extends Module {
    public static final OptionDouble horizontal_vel = new OptionDouble("Horizontal", 0D, 0D, 100D);
    public static final OptionDouble vertical_vel = new OptionDouble("Vertical", 0D, 0D, 100D);
    public static final OptionBoolean explosions = new OptionBoolean("Explosions", true);
    public static final OptionBoolean bobbers = new OptionBoolean("Bobbers", true);
    public static final OptionBoolean noPush = new OptionBoolean("No push", true);

    public VelocityModule() {
        super("Velocity", null, "NONE", Color.CYAN, ModuleType.COMBAT);
        addOption(horizontal_vel);
        addOption(vertical_vel);
        addOption(explosions);
        addOption(bobbers);
        addOption(noPush);
        endOption();
    }

    @Override
    public String getMeta() { return String.format("H:%s%% V:%s%%", horizontal_vel.getValue(), vertical_vel.getValue()); }

    @EventHandler
    private Listener<PlayerPushOutOfBlocksEvent> playerPushOutOfBlocksEventListener = new Listener<>(event -> {
        if(noPush.getValue())
            event.cancel();
    });

    @EventHandler
    private Listener<PlayerPushedByWaterEvent> playerPushedByWaterEventListener = new Listener<>(event -> {
        if(noPush.getValue())
            event.cancel();
    });

    @EventHandler
    private Listener<PlayerApplyCollisionEvent> applyCollisionEventListener = new Listener<>(event -> {
        if(noPush.getValue())
            event.cancel();
    });

    @EventHandler
    private Listener<NetworkPacketEvent> packetEventListener = new Listener<>(event -> {
        if(mc.player == null)
            return;

        if (event.getPacket() instanceof SPacketEntityStatus && bobbers.getValue()) {
            final SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
            if (packet.getOpCode() == 31) {
                final Entity entity = packet.getEntity(mc.world);
                if (entity != null && entity instanceof EntityFishHook) {
                    final EntityFishHook fishHook = (EntityFishHook) entity;
                    if (fishHook.caughtEntity == mc.player) {
                        event.cancel();
                    }
                }
            }
        }
        if (event.getPacket() instanceof SPacketEntityVelocity) {
            final SPacketEntityVelocity packet = (SPacketEntityVelocity)event.getPacket();
            if (packet.getEntityID() == mc.player.getEntityId()) {
                if (horizontal_vel.getValue().intValue() == 0 && vertical_vel.getValue().intValue() == 0) {
                    event.cancel();
                    return;
                }

                if (horizontal_vel.getValue() != 100) {
                    packet.motionX = packet.motionX / 100 * horizontal_vel.getValue().intValue();
                    packet.motionZ = packet.motionZ / 100 * horizontal_vel.getValue().intValue();
                }

                if (vertical_vel.getValue() != 100)
                    packet.motionY = packet.motionY / 100 * vertical_vel.getValue().intValue();
            }
        }
        if (event.getPacket() instanceof SPacketExplosion && this.explosions.getValue()) {
            final SPacketExplosion packet = (SPacketExplosion)event.getPacket();

            if (horizontal_vel.getValue() == 0 && vertical_vel.getValue() == 0) {
                event.cancel();
                return;
            }

            if (horizontal_vel.getValue() != 100)
            {
                packet.motionX = packet.motionX / 100 * horizontal_vel.getValue().intValue();
                packet.motionZ = packet.motionZ / 100 * horizontal_vel.getValue().intValue();
            }

            if (vertical_vel.getValue() != 100)
                packet.motionY = packet.motionY / 100 * vertical_vel.getValue().intValue();
        }
    });
}