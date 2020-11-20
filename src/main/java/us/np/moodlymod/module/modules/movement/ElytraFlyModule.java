package us.np.moodlymod.module.modules.movement;

import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.text.TextComponentString;
import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.event.custom.entityplayer.PlayerTravelEvent;
import us.np.moodlymod.event.custom.networkmanager.NetworkPacketEvent;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.BetterMode;
import us.np.moodlymod.module.option.OptionBetterMode;
import us.np.moodlymod.module.option.OptionBoolean;
import us.np.moodlymod.module.option.OptionDouble;
import us.np.moodlymod.util.ChatColor;
import us.np.moodlymod.util.MathUtil;
import us.np.moodlymod.util.Timer;

import java.awt.*;

public class ElytraFlyModule extends Module {
    public static OptionBetterMode mode;
    public static OptionDouble speed, downSpeed, glideSpeed, upSpeed;
    public static OptionBoolean accelerate;
    public static OptionDouble accelerationTimer, rotationPitch;
    public static OptionBoolean cancelInWater;
    public static OptionDouble cancelAtHeight;
    public static OptionBoolean instantFly, equipElytra, pitchSpoof;

    private Timer _packetTimer = new Timer();
    private Timer _accelerationTimer = new Timer();
    private Timer _accelerationResetTimer = new Timer();
    private Timer _instantFlyTimer = new Timer();
    private boolean sendMessage = false;
    private int elytraSlot = -1;

    public ElytraFlyModule() {
        super("ElytraFly", null, "NONE", Color.BLACK, ModuleType.MOVEMENT);
        addOption(mode = new OptionBetterMode("Mode", 2, BetterMode.construct("Normal", "Superior", "Control")));
        addOption(speed = new OptionDouble("Speed", 1.82D, 0D, 10D));
        addOption(downSpeed = new OptionDouble("Down Speed", 1.82D, 0D, 10D));
        addOption(glideSpeed = new OptionDouble("Glide Speed", 1D, 0D, 10D));
        addOption(upSpeed = new OptionDouble("Up Speed", 2D, 0D, 10D));
        addOption(accelerate = new OptionBoolean("Accelerate", true));
        addOption(accelerationTimer = new OptionDouble("Acceleration Timer", 1D, 0D, 10D));
        addOption(rotationPitch = new OptionDouble("Rotation Pitch", 0D, -90D, 90D));
        addOption(cancelInWater = new OptionBoolean("Cancel In Water", true));
        addOption(cancelAtHeight = new OptionDouble("Cancel At Height", 5D, 0D, 10D));
        addOption(instantFly = new OptionBoolean("InstantFly", true));
        addOption(equipElytra = new OptionBoolean("Equip Elytra", false));
        addOption(pitchSpoof = new OptionBoolean("PitchSpoof", false));
        endOption();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        elytraSlot = -1;
        if(equipElytra.getValue()) {
            if(mc.player != null && mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.ELYTRA) {
                for(int i = 0; i < 44; i++) {
                    ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
                    if(itemStack.isEmpty() || itemStack.getItem() != Items.ELYTRA) continue;

                    ItemElytra itemElytra = (ItemElytra)itemStack.getItem();
                    elytraSlot = i;
                    break;
                }
                if(elytraSlot != -1) {
                    boolean armorChest = mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.AIR;
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, elytraSlot, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, mc.player);
                    if(armorChest) mc.playerController.windowClick(mc.player.inventoryContainer.windowId, elytraSlot, 0, ClickType.PICKUP, mc.player);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if(mc.player == null) return;
        if(elytraSlot != -1) {
            boolean hasItem = !mc.player.inventory.getStackInSlot(elytraSlot).isEmpty() || mc.player.inventory.getStackInSlot(elytraSlot).getItem() != Items.AIR;
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, elytraSlot, 0, ClickType.PICKUP, mc.player);
            if (hasItem) mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, mc.player);
        }
    }

    @EventHandler
    private Listener<PlayerTravelEvent> playerTravelEventListener = new Listener<>(event -> {
        if(mc.player == null) return;
        if(mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.ELYTRA) return;

        if(!mc.player.isElytraFlying()) {
            if(!mc.player.onGround && instantFly.getValue() && _instantFlyTimer.passed(1000)) {
                _instantFlyTimer.reset();
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            }
            return;
        }

        /*
            * Normal   == 0
              Superior == 1
            * Control  == 2
                             */

        switch (mode.getMode()) {
            case 0: handleNormal(event); break;
            case 1: handleSuperior(event); break;
            case 2: handleControl(event); break;
            default: break;
        }
    });

    public void accelerate() {
        if(_accelerationResetTimer.passed(accelerationTimer.getValue() * 1000)) {
            _accelerationResetTimer.reset();
            _accelerationTimer.reset();
            sendMessage = false;
        }
        float speed = this.speed.getValue().floatValue();
        double[] dir = MathUtil.directionSpeed(speed);

        boolean cum = mc.player.movementInput.moveStrafe != 0 || mc.player.movementInput.moveForward != 0;
        mc.player.motionX = cum ? dir[0] : 0;
        mc.player.motionY = mc.player.movementInput.sneak ? -downSpeed.getValue() : -(glideSpeed.getValue() / 10000f);
        mc.player.motionZ = cum ? dir[1] : 0;

        mc.player.prevLimbSwingAmount = 0;
        mc.player.limbSwingAmount = 0;
        mc.player.limbSwing = 0;
    }

    public void handleNormal(PlayerTravelEvent event) {
        double height = mc.player.posY;
        if(height <= cancelAtHeight.getValue() && !sendMessage) {
            mc.player.sendMessage(new TextComponentString(ChatColor.parse("&", "&eHEY! &cYou must go up because your y-height is lower than cancelAtHeight value!")));
            sendMessage = true;
            return;
        }
        boolean moveKeyDown = mc.player.movementInput.moveForward > 0 || mc.player.movementInput.moveStrafe > 0;
        boolean _cancelInWater = !mc.player.isInWater() && !mc.player.isInLava() && cancelInWater.getValue();
        if(mc.player.movementInput.jump) {
            event.cancel();
            accelerate();
            return;
        }
        if(moveKeyDown) _accelerationTimer.resetTimeSkipTo(-accelerationTimer.getValue().longValue());
        else if((mc.player.rotationPitch <= rotationPitch.getValue()) && _cancelInWater && accelerate.getValue() && _accelerationTimer.passed(accelerationTimer.getValue())) {
            accelerate();
            return;
        }
        event.cancel();
        accelerate();
    }

    public void handleSuperior(PlayerTravelEvent event) {
        if(mc.player.movementInput.jump) {
            double motion = Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
            if(motion > 1D) return;

            double[] dir = MathUtil.directionSpeedNoForward(speed.getValue());
            mc.player.motionX = dir[0];
            mc.player.motionY = -(glideSpeed.getValue() / 10000f);
            mc.player.motionZ = dir[1];

            event.cancel();
            return;
        }
        mc.player.setVelocity(0, 0, 0);

        double[] dir = MathUtil.directionSpeed(speed.getValue());
        if(mc.player.movementInput.moveStrafe != 0 || mc.player.movementInput.moveForward != 0)
        {
            mc.player.motionX = dir[0];
            mc.player.motionY = -(glideSpeed.getValue() / 10000f);
            mc.player.motionZ = dir[1];
        }
        if(mc.player.movementInput.sneak) mc.player.motionY = -downSpeed.getValue();
        mc.player.prevLimbSwingAmount = 0;
        mc.player.limbSwingAmount = 0;
        mc.player.limbSwing = 0;
    }

    public void handleControl(PlayerTravelEvent event) {
        double[] dir = MathUtil.directionSpeed(speed.getValue());
        if (mc.player.movementInput.moveStrafe != 0 || mc.player.movementInput.moveForward != 0)
        {
            mc.player.motionX = dir[0];
            mc.player.motionZ = dir[1];
            mc.player.motionX -= (mc.player.motionX*(Math.abs(mc.player.rotationPitch)+90)/90) - mc.player.motionX;
            mc.player.motionZ -= (mc.player.motionZ*(Math.abs(mc.player.rotationPitch)+90)/90) - mc.player.motionZ;
        } else {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }
        mc.player.motionY = (-MathUtil.degToRad(mc.player.rotationPitch)) * mc.player.movementInput.moveForward;

        mc.player.prevLimbSwingAmount = 0;
        mc.player.limbSwingAmount = 0;
        mc.player.limbSwing = 0;
        event.cancel();
    }

    @EventHandler
    private Listener<NetworkPacketEvent> networkPacketEventListener = new Listener<>(event -> {
        Packet packet = event.getPacket();
        if(packet instanceof CPacketPlayer && pitchSpoof.getValue() && mc.player.isElytraFlying())
            if(packet instanceof CPacketPlayer.PositionRotation) {
                CPacketPlayer.PositionRotation packetC = (CPacketPlayer.PositionRotation)packet;
                mc.getConnection().sendPacket(new CPacketPlayer.Position(packetC.x, packetC.y, packetC.z, packetC.onGround));
                event.cancel();
            } else if(packet instanceof CPacketPlayer.Rotation) event.cancel();
    });
}