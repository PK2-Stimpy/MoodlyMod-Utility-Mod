package us.np.moodlymod.module.modules.combat;

import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import us.np.moodlymod.event.custom.CustomEvent;
import us.np.moodlymod.event.custom.entityplayersp.PlayerMotionUpdateEvent;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.BetterMode;
import us.np.moodlymod.module.option.OptionBetterMode;
import us.np.moodlymod.util.PlayerUtil;
import us.np.moodlymod.util.RotationSpoof;

import java.awt.*;

public class AimbotModule extends Module {
    public static final OptionBetterMode mode = new OptionBetterMode("Mode", 0, BetterMode.construct("PACKET", "CLIENT"));

    public AimbotModule() {
        super("Aimbot", null, "NONE", Color.RED, ModuleType.COMBAT);
        addOption(mode);
        endOption();
    }

    public RotationSpoof rotationSpoof = null;

    @Override
    public String getMeta() { return mode.getDisplayName(); }

    @EventHandler
    private Listener<PlayerMotionUpdateEvent> motionUpdateEventListener = new Listener<>(event -> {
        if((event.getEra() != CustomEvent.Era.PRE) || (rotationSpoof == null))
            return;
        event.cancel();

        boolean sprinting = mc.player.isSprinting();
        boolean sneaking = mc.player.isSneaking();

        if (sprinting != mc.player.serverSprintState) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, sprinting ? CPacketEntityAction.Action.START_SPRINTING : CPacketEntityAction.Action.STOP_SPRINTING));
            mc.player.serverSprintState = sprinting;
        }
        if (sneaking != mc.player.serverSneakState) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, sprinting ? CPacketEntityAction.Action.START_SNEAKING : CPacketEntityAction.Action.STOP_SNEAKING));
            mc.player.serverSprintState = sprinting;
        }

        if(PlayerUtil.isCurrentViewEntity()) {
            float pitch = mc.player.rotationPitch;
            float yaw = mc.player.rotationYaw;

            if(rotationSpoof != null) {
                pitch = rotationSpoof.pitch;
                yaw = rotationSpoof.yaw;

                switch (mode.getMode()) {
                    case 1:
                        mc.player.rotationPitch = pitch;
                        mc.player.rotationYaw = yaw;
                        return;
                    default:
                        mc.player.rotationYawHead = yaw;
                        return;
                }
            }

            AxisAlignedBB axisalignedbb = mc.player.getEntityBoundingBox();
            double posXDif = mc.player.posX - mc.player.lastReportedPosX;
            double posYDif = axisalignedbb.minY - mc.player.lastReportedPosY;
            double posZDif = mc.player.posZ - mc.player.lastReportedPosZ;
            double posYawDif = (double)(yaw - mc.player.lastReportedYaw);
            double posRotDif = (double)(pitch - mc.player.lastReportedPitch);
            ++mc.player.positionUpdateTicks;
            boolean movedXYZ = Math.sqrt(posXDif) + Math.sqrt(posYDif) + Math.sqrt(posZDif) > 9.0E-4D || mc.player.positionUpdateTicks >= 20;
            boolean movedRot = posYawDif != 0.0D || posRotDif != 0.0D;

            if (mc.player.isRiding()) {
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.motionX, -999.0D, mc.player.motionZ, yaw, pitch, mc.player.onGround));
                movedXYZ = false;
            }
            else if (movedXYZ && movedRot) mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, axisalignedbb.minY, mc.player.posZ, yaw, pitch, mc.player.onGround));
            else if (movedXYZ) mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, axisalignedbb.minY, mc.player.posZ, mc.player.onGround));
            else if (movedRot) mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch, mc.player.onGround));
            else if (mc.player.prevOnGround != mc.player.onGround) mc.player.connection.sendPacket(new CPacketPlayer(mc.player.onGround));

            if (movedXYZ) {
                mc.player.lastReportedPosX = mc.player.posX;
                mc.player.lastReportedPosY = axisalignedbb.minY;
                mc.player.lastReportedPosZ = mc.player.posZ;
                mc.player.positionUpdateTicks = 0;
            }
            if (movedRot) {
                mc.player.lastReportedYaw = yaw;
                mc.player.lastReportedPitch = pitch;
            }

            mc.player.prevOnGround = mc.player.onGround;
            mc.player.autoJumpEnabled = mc.player.mc.gameSettings.autoJump;
        }
    });
}