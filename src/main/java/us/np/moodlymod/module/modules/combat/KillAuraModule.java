package us.np.moodlymod.module.modules.combat;

import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.managers.TickRateManager;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.BetterMode;
import us.np.moodlymod.module.option.OptionBetterMode;
import us.np.moodlymod.module.option.OptionBoolean;
import us.np.moodlymod.module.option.OptionDouble;
import us.np.moodlymod.util.*;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Comparator;

public class KillAuraModule extends Module {
    public static final OptionBetterMode mode = new OptionBetterMode("Mode", 0, BetterMode.construct("Closest", "Priority", "Switch"));
    public static final OptionDouble distance = new OptionDouble("Distance", 5D, 0D, 10D);
    public static final OptionBoolean hitDelay = new OptionBoolean("Hit delay", true);
    public static final OptionBoolean tpsSync = new OptionBoolean("Tps Sync", false);
    public static final OptionBoolean players = new OptionBoolean("Players", true);
    public static final OptionBoolean monsters = new OptionBoolean("Monsters", true);
    public static final OptionBoolean neutrals = new OptionBoolean("Neutrals", false);
    public static final OptionBoolean animals = new OptionBoolean("Animals", false);
    public static final OptionBoolean tamed = new OptionBoolean("Tamed", false);
    public static final OptionBoolean projectiles = new OptionBoolean("Projectiles", false);
    public static final OptionBoolean onlySword = new OptionBoolean("Only sword", false);
    public static final OptionBoolean only32k = new OptionBoolean("Only 32k", false);
    public static final OptionBoolean pauseIfCrystal = new OptionBoolean("Crystal pause", false);
    public static final OptionBoolean pauseIfEating = new OptionBoolean("Eating pause", false);
    public static final OptionBoolean autoSwitch = new OptionBoolean("AutoSwitch", false);
    public static final OptionDouble ticks = new OptionDouble("Ticks", 10D, 0D, 40D);
    public static final OptionDouble iterations = new OptionDouble("Iterations", 1D, 1D, 10D);

    public KillAuraModule() {
        super("KillAura", null, "NONE", Color.RED, ModuleType.COMBAT);
        addOption(mode);
        addOption(distance);
        addOption(hitDelay);
        addOption(tpsSync);
        addOption(players);
        addOption(monsters);
        addOption(neutrals);
        addOption(animals);
        addOption(tamed);
        addOption(projectiles);
        addOption(onlySword);
        addOption(only32k);
        addOption(pauseIfCrystal);
        addOption(pauseIfEating);
        addOption(autoSwitch);
        addOption(ticks);
        addOption(iterations);
        endOption();
    }

    private Entity currentTarget;
    private AimbotModule aimbotModule = null;
    private Timer aimbotResetTimer = new Timer();
    private int remainingTicks = 0;

    @Override
    public void onEnable() {
        super.onEnable();

        remainingTicks = 0;
        if(aimbotModule == null)
            aimbotModule = (AimbotModule) MoodlyMod.moduleManager.getModuleByClass(AimbotModule.class);
        if(!aimbotModule.isEnabled())
            aimbotModule.toggle(true);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if(aimbotModule != null)
            aimbotModule.rotationSpoof = null;
    }

    @Override
    public String getMeta() {
        return mode.getDisplayName();
    }

    private boolean isValidTarget(Entity entity, @Nullable Entity toIgnore) {
        if (!(entity instanceof EntityLivingBase)) {
            boolean isProjectile = (entity instanceof EntityShulkerBullet || entity instanceof EntityFireball);
            if ((!isProjectile) || (isProjectile && !projectiles.getValue()))
                return false;
        }

        if (toIgnore != null && entity == toIgnore)
            return false;

        if (entity instanceof EntityPlayer) {
            if (entity == mc.player)
                return false;
            if (!players.getValue())
                return false;
            if (MoodlyMod.friendsUtil.isFriend(entity.getName()))
                return false;
        }

        boolean neutralMob = entity instanceof EntityPigZombie || entity instanceof EntityWolf || entity instanceof EntityEnderman;
        boolean hostileMob = (entity.isCreatureType(EnumCreatureType.MONSTER, false) && !neutralMob);
        if (hostileMob && !monsters.getValue())
            return false;

        if (EntityUtil.isPassive(entity)) {
            if (entity instanceof AbstractChestHorse) {
                AbstractChestHorse horse = (AbstractChestHorse)entity;
                if (horse.isTame() && !tamed.getValue())
                    return false;
            }
            if (!animals.getValue())
                return false;
        }

        if (hostileMob && !monsters.getValue())
            return false;
        if (neutralMob && !neutrals.getValue())
            return false;

        boolean healthCheck = true;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase base = (EntityLivingBase)entity;
            healthCheck = !base.isDead && base.getHealth() > 0.0f;
        }

        return healthCheck && entity.getDistance(entity) <= distance.getValue();
    }

    @EventHandler
    private Listener<TickEvent.ClientTickEvent> tickEventListener = new Listener<>(event -> {
        if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword)) {
            if (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL && pauseIfCrystal.getValue())
                return;
            if (mc.player.getHeldItemMainhand().getItem() == Items.GOLDEN_APPLE && pauseIfEating.getValue())
                return;

            int slot = -1;
            if (autoSwitch.getValue())
                for(int y = 0; y < 9; y++)
                    if (mc.player.inventory.getStackInSlot(y).getItem() instanceof ItemSword) {
                        slot = y;
                        mc.player.inventory.currentItem = slot;
                        mc.playerController.updateController();
                        break;
                    }
            if (onlySword.getValue() && slot == -1)
                return;
        }

        if(only32k.getValue() && !ItemUtil.is32k(mc.player.getHeldItemMainhand()))
            return;

        if(aimbotResetTimer.passed(5000)) {
            aimbotResetTimer.reset();
            aimbotModule.rotationSpoof = null;
        }
        if(remainingTicks > 0) --remainingTicks;

        Entity targetToHit = currentTarget;
        switch (mode.getMode()) {
            case 0:
                targetToHit = mc.world.loadedEntityList.stream()
                        .filter(entity -> isValidTarget(entity, null))
                        .min(Comparator.comparing(entity -> mc.player.getDistance(entity)))
                        .orElse(null);
                break;
            case 1:
                if(targetToHit == null)
                    targetToHit = mc.world.loadedEntityList.stream()
                            .filter(entity -> isValidTarget(entity, null))
                            .min(Comparator.comparing(entity -> mc.player.getDistance(entity)))
                            .orElse(null);

                break;
            case 2:
                targetToHit = mc.world.loadedEntityList.stream()
                        .filter(p_Entity -> isValidTarget(p_Entity, null))
                        .min(Comparator.comparing(p_Entity -> mc.player.getDistance(p_Entity)))
                        .orElse(null);

                if (targetToHit == null)
                    targetToHit = currentTarget;
                break;
            default:
                break;
        }

        if (targetToHit == null || targetToHit.getDistance(mc.player) > distance.getValue()) {
            currentTarget = null;
            return;
        }

        float[] rotation = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), targetToHit.getPositionEyes(mc.getRenderPartialTicks()));
        aimbotModule.rotationSpoof = new RotationSpoof(rotation[0], rotation[1]);
        final float ticks = 20.0f - TickRateManager.INSTANCE.getTickRate();
        final boolean attackReady = hitDelay.getValue() ? (mc.player.getCooledAttackStrength(tpsSync.getValue() ? -ticks : 0.0f) >= 1) : true;
        if (!attackReady)
            return;
        if (!hitDelay.getValue() && remainingTicks > 0)
            return;

        remainingTicks = KillAuraModule.ticks.getValue().intValue();
        for (int y = 0; y < iterations.getValue().intValue(); ++y) {
            mc.player.connection.sendPacket(new CPacketUseEntity(targetToHit));
            mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.player.resetCooldown();
        }
    });
}