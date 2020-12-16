package us.np.moodlymod.module.modules.movement;

import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayer;
import us.np.moodlymod.event.custom.CustomEvent;
import us.np.moodlymod.event.custom.entityplayersp.PlayerMoveEvent;
import us.np.moodlymod.event.custom.entityplayersp.PlayerUpdateEvent;
import us.np.moodlymod.event.custom.networkmanager.NetworkPacketEvent;
import us.np.moodlymod.mixin.accessors.ICPacketPlayer;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.BetterMode;
import us.np.moodlymod.module.option.OptionBetterMode;
import us.np.moodlymod.module.option.OptionDouble;

import java.awt.*;
import java.util.Objects;

public class SpeedModule extends Module {
    public static final OptionBetterMode mode = new OptionBetterMode("Mode", 0, BetterMode.construct("NCP", "NCPHOP", "PACKET"));
    public static final OptionDouble speed = new OptionDouble("Speed", .25D, .1D, 10D);

    private int stage = 1;
    private boolean speedTick;
    private double moveSpeed, lastDist;
    private int waitCounter;
    private int forward = 1;
    public SpeedModule() {
        super("Speed", null, "NONE", Color.CYAN, ModuleType.MOVEMENT);
        addOption(mode);
        addOption(speed);
        endOption();
    }

    @EventHandler
    private Listener<NetworkPacketEvent> packetEventListener = new Listener<>(event -> {
        if (mc.world == null || mc.player == null)
            return;
        if(event.getEra() == CustomEvent.Era.PRE) {
            if (mode.getMode() == 0 && event.getPacket() instanceof CPacketPlayer && mc.player.onGround && !mc.player.movementInput.jump) {
                if (!speedTick) {
                    final boolean isUnderBlocks = !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0, 1, 0)).isEmpty();
                    ICPacketPlayer packet = (ICPacketPlayer)event.getPacket();
                    packet.setY(packet.getY() + (isUnderBlocks ? 0.2 : 0.4));
                }
                mc.player.motionX *= (speedTick ? 2D : 0.701);
                mc.player.motionZ *= (speedTick ? 2D : 0.701);
            }
        }
    });

    @EventHandler
    private Listener<PlayerUpdateEvent> updateEventListener = new Listener<>(event -> {
        if (mc.world == null || mc.player == null)
            return;
        switch (mode.getMode()) {
            case 0:
                if (event.getEra() == CustomEvent.Era.PRE)
                    lastDist = Math.sqrt(((mc.player.posX - mc.player.prevPosX) * (mc.player.posX - mc.player.prevPosX)) + ((mc.player.posZ - mc.player.prevPosZ) * (mc.player.posZ - mc.player.prevPosZ)));
                break;
            case 2:
                if ((this.mc.player.moveForward != 0.0f || this.mc.player.moveStrafing != 0.0f)) {
                    for (double x = 0.0625; x < speed.getValue(); x += 0.262) {
                        final double[] dir = getDirectionalSpeed(x);
                        this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX + dir[0], this.mc.player.posY, this.mc.player.posZ + dir[1], this.mc.player.onGround));
                    }
                    this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX + this.mc.player.motionX, -30, this.mc.player.posZ + this.mc.player.motionZ, this.mc.player.onGround));
                }
                break;
            default:
                break;
        }
    });

    private Listener<PlayerMoveEvent> moveEventListener = new Listener<>(event -> {
        if (mc.world == null || mc.player == null)
            return;
        if (mode.getMode() == 1) {
            switch (stage) {
                case 0:
                    ++stage;
                    lastDist = 0.0D;
                    break;
                case 2:
                    double motionY = 0.40123128;
                    if ((mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F) && mc.player.onGround) {
                        if (mc.player.isPotionActive(MobEffects.JUMP_BOOST))
                            motionY += ((mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
                        event.y = (mc.player.motionY = motionY);
                        moveSpeed *= 2.149;
                    }
                    break;
                case 3:
                    moveSpeed = lastDist - (0.76 * (lastDist - getBaseMoveSpeed()));
                    break;
                default:
                    if ((mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0D, mc.player.motionY, 0.0D)).size() > 0 || mc.player.collidedVertically) && stage > 0) {
                        stage = mc.player.moveForward == 0.0F && mc.player.moveStrafing == 0.0F ? 0 : 1;
                    }
                    moveSpeed = lastDist - lastDist / 159.0D;
                    break;
            }
            moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
            double forward = mc.player.movementInput.moveForward, strafe = mc.player.movementInput.moveStrafe, yaw = mc.player.rotationYaw;
            if (forward == 0.0F && strafe == 0.0F) {
                event.x = (0);
                event.z = (0);
            }
            if (forward != 0 && strafe != 0) {
                forward = forward * Math.sin(Math.PI / 4);
                strafe = strafe * Math.cos(Math.PI / 4);
            }
            event.x = ((forward * moveSpeed * -Math.sin(Math.toRadians(yaw)) + strafe * moveSpeed * Math.cos(Math.toRadians(yaw))) * 0.99D);
            event.z = ((forward * moveSpeed * Math.cos(Math.toRadians(yaw)) - strafe * moveSpeed * -Math.sin(Math.toRadians(yaw))) * 0.99D);
            ++stage;
        }
    });

    private double getBaseMoveSpeed() {
        double baseSpeed = 0.272;
        if (mc.player.isPotionActive(MobEffects.SPEED)) {
            final int amplifier = Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
            baseSpeed *= 1.0 + (0.2 * amplifier);
        }
        return baseSpeed;
    }

    private double[] getDirectionalSpeed(final double speed) {
        float forward = mc.player.movementInput.moveForward;
        float side = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        final double posX = forward * speed * cos + side * speed * sin;
        final double posZ = forward * speed * sin - side * speed * cos;
        return new double[]{posX, posZ};
    }
}