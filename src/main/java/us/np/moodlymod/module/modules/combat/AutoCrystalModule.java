package us.np.moodlymod.module.modules.combat;

import static us.np.moodlymod.MoodlyMod.debug;

import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;
import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.Wrapper;
import us.np.moodlymod.event.custom.CustomEvent;
import us.np.moodlymod.event.custom.entityplayersp.PlayerMotionUpdateEvent;
import us.np.moodlymod.event.custom.networkmanager.NetworkPacketEvent;
import us.np.moodlymod.event.custom.render.RenderEvent;
import us.np.moodlymod.event.custom.world.EntityRemovedEvent;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.BetterMode;
import us.np.moodlymod.module.option.OptionBetterMode;
import us.np.moodlymod.module.option.OptionBoolean;
import us.np.moodlymod.module.option.OptionDouble;
import us.np.moodlymod.util.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;


/**
 * @apiNote Checks: 'NetworkPacketEvent', 'PlayerMotionUpdateEvent', 'EntityRemovedEvent'
 * @apiNote 'TickEvent.PlayerTickEvent', 'RenderEvent'.
 *
 * */
public class AutoCrystalModule extends Module {
    public static final OptionBetterMode breakMode = new OptionBetterMode("BreakMode", 1, BetterMode.construct("Always", "Smart", "OnlyOwn"));
    public static final OptionBetterMode placeMode = new OptionBetterMode("PlaceMode", 0, BetterMode.construct("Most", "Lethal"));
    public static final OptionDouble placeRadius = new OptionDouble("Place radius", 5D, 0D, 5D);
    public static final OptionDouble breakRadius = new OptionDouble("Break radius", 5D, 0D, 5D);
    public static final OptionDouble wallsRange = new OptionDouble("Walls range", 4.5D, 0D, 5D);
    public static final OptionBoolean multiPlace = new OptionBoolean("MultiPlace", true);
    public static final OptionDouble ticks = new OptionDouble("Ticks", 2D, 0D, 20D);
    public static final OptionDouble minDmg = new OptionDouble("MinDmg", 4D, 0D, 20D);
    public static final OptionDouble maxSelfDmg = new OptionDouble("MaxSelfDmg", 8D, 0D, 20D);
    public static final OptionDouble facePlace = new OptionDouble("FacePlace", 8D, 0D, 20D);
    public static final OptionBoolean autoSwitch = new OptionBoolean("AutoSwitch", false);
    public static final OptionBoolean pauseIfHittingBlock = new OptionBoolean("Pause if hitting block", true);
    public static final OptionBoolean pauseWhileEating = new OptionBoolean("Pause while eating", true);
    public static final OptionBoolean noSuicide = new OptionBoolean("No suicide", false);
    public static final OptionBoolean antiWeakness = new OptionBoolean("Anti weakness", false);
    /* Render */
    public static final OptionBoolean render = new OptionBoolean("Render", true);
    public static final OptionDouble red = new OptionDouble("Red", 51D, 0D, 255D);
    public static final OptionDouble green = new OptionDouble("Green", 255D, 0D, 255D);
    public static final OptionDouble blue = new OptionDouble("Blue", 243D, 0D, 255D);
    public static final OptionDouble alpha = new OptionDouble("Alpha", 153D, 0D, 255D);

    public AutoCrystalModule() {
        super("AutoCrystal", null, "NONE", Color.RED, ModuleType.COMBAT);
        INSTANCE = this;

        addOption(breakMode);
        addOption(placeMode);
        addOption(placeMode);
        addOption(breakMode);
        addOption(wallsRange);
        addOption(multiPlace);
        addOption(ticks);
        addOption(minDmg);
        addOption(maxSelfDmg);
        addOption(facePlace);
        addOption(autoSwitch);
        addOption(pauseIfHittingBlock);
        addOption(pauseWhileEating);
        addOption(noSuicide);
        addOption(antiWeakness);
        addOption(render);
        addOption(red);
        addOption(green);
        addOption(blue);
        addOption(alpha);
        endOption();
    }

    private AutoCrystalModule INSTANCE;
    public static Timer _removeVisualTimer = new Timer();
    private Timer _rotationResetTimer = new Timer();
    private ConcurrentLinkedQueue<BlockPos> _placedCrystals = new ConcurrentLinkedQueue<>();
    private ConcurrentHashMap<BlockPos, Float> _placedCrystalsDamage = new ConcurrentHashMap<>();
    private ICamera camera = new Frustum();
    private double[] _rotations = null;
    private ConcurrentHashMap<EntityEnderCrystal, Integer> _attackedEnderCrystals = new ConcurrentHashMap<>();
    private String _lastTarget = null;
    private int _remainingTicks;
    private BlockPos _lastPlaceLocation = BlockPos.ORIGIN;


    @Override
    public void onEnable() {
        super.onEnable();

        _placedCrystals.clear();
        _placedCrystalsDamage.clear();
        _remainingTicks = 0;
        _lastPlaceLocation = BlockPos.ORIGIN;
    }

    @EventHandler
    private Listener<EntityRemovedEvent> entityRemovedEventListener = new Listener<>(event -> {
        if(event.getEntity() instanceof EntityEnderCrystal)
            _attackedEnderCrystals.remove((EntityEnderCrystal)event.getEntity());
    });

    private boolean validateCrystal(EntityEnderCrystal crystal) {
        if(mc.player == null) return false;
        if(crystal == null || crystal.isDead) return false;
        if (_attackedEnderCrystals.containsKey(crystal) && _attackedEnderCrystals.get(crystal) > 5) return false;
        if(crystal.getDistance(mc.player) > (!mc.player.canEntityBeSeen(crystal) ? wallsRange.getValue() : breakRadius.getValue())) return false;
        switch (breakMode.getMode()) {
            case 2: return crystal.getDistance(crystal.posX, crystal.posY, crystal.posZ) <= 3;
            case 1:
                float selfDamage = CrystalUtils.calculateDamage(mc.world, crystal.posX, crystal.posY, crystal.posZ, mc.player, 0);
                if(selfDamage > maxSelfDmg.getValue()) return false;
                if(noSuicide.getValue() && selfDamage >= mc.player.getHealth()+mc.player.getAbsorptionAmount()) return false;
                for(EntityPlayer entityPlayer : mc.world.playerEntities) {
                    if(entityPlayer == mc.player || MoodlyMod.friendsUtil.isFriend(entityPlayer.getName()) || mc.player.isDead || (mc.player.getHealth() + mc.player.getAbsorptionAmount()) <= 0f) continue;
                    float minDmg = this.minDmg.getValue().floatValue();
                    if(entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount() <= facePlace.getValue()) minDmg = 1f;
                    float calculatedDmg = CrystalUtils.calculateDamage(mc.world, crystal.posX, crystal.posY, crystal.posZ, entityPlayer, 0);
                    if(calculatedDmg > minDmg)
                        return true;
                }
                return false;
            default:
                break;
        }
        return true;
    }

    public EntityEnderCrystal getNearestCrystalTo(Entity entity) { if(mc.player == null) return null; return mc.world.getLoadedEntityList().stream().filter(e -> e instanceof EntityEnderCrystal && validateCrystal((EntityEnderCrystal)e)).map(e -> (EntityEnderCrystal)e).min(Comparator.comparing(e -> entity.getDistance(e))).orElse(null); }
    public void addAttackedCrystal(EntityEnderCrystal enderCrystal) {
        if(mc.player == null) return;
        if(_attackedEnderCrystals.contains(enderCrystal)) {
            int value = _attackedEnderCrystals.get(enderCrystal);
            _attackedEnderCrystals.put(enderCrystal, value + 1);
        } else _attackedEnderCrystals.put(enderCrystal, 1);
    }
    public boolean verifyCrystalBlocks(BlockPos pos) {
        if(mc.player == null) return false;
        if(mc.player.getDistanceSq(pos) > placeRadius.getValue()*placeRadius.getValue()) return false;
        if(wallsRange.getValue() > 0)
            if(!PlayerUtil.canSeeBlock(pos))
                if(pos.getDistance((int)mc.player.posX, (int)mc.player.posY, (int)mc.player.posZ) > wallsRange.getValue())
                    return false;
        float selfDmg = CrystalUtils.calculateDamage(mc.world, pos.getX()+.5, pos.getY()+1.0, pos.getZ()+.5, mc.player, 0);
        if(selfDmg > maxSelfDmg.getValue()) return false;
        if(noSuicide.getValue() && selfDmg >= mc.player.getHealth()+mc.player.getAbsorptionAmount()) return false;
        return true;
    }

    @EventHandler
    private Listener<TickEvent.PlayerTickEvent> playerTickEventListener = new Listener<>(event -> {
        if (_removeVisualTimer.passed(1000)) {
            _removeVisualTimer.reset();
            if (!_placedCrystals.isEmpty()) {
                BlockPos removed = _placedCrystals.remove();
                if (removed != null)
                    _placedCrystalsDamage.remove(removed);
            }
            _attackedEnderCrystals.clear();
        }
        if (needPause()) {
            _remainingTicks = 0;
            return;
        }
        if (placeMode.getMode() == 1 && _lastPlaceLocation != BlockPos.ORIGIN) {
            float damage = 0f;
            EntityPlayer trappedTarget = null;
            for (EntityPlayer player : mc.world.playerEntities) {
                if (player == mc.player || MoodlyMod.friendsUtil.isFriend(player.getName()) || mc.player.isDead || (mc.player.getHealth() + mc.player.getAbsorptionAmount()) <= 0.0f)
                    continue;
                float minDamage = minDmg.getValue().floatValue();
                if (player.getHealth() + player.getAbsorptionAmount() <= facePlace.getValue())
                    minDamage = 1f;
                float calculatedDamage = CrystalUtils.calculateDamage(mc.world, _lastPlaceLocation.getX() + 0.5, _lastPlaceLocation.getY() + 1.0, _lastPlaceLocation.getZ() + 0.5, player, 0);
                if (calculatedDamage >= minDamage && calculatedDamage > damage) {
                    damage = calculatedDamage;
                    trappedTarget = player;
                }
            }

            if (damage == 0f || trappedTarget == null)
                _lastPlaceLocation = BlockPos.ORIGIN;
        }
        if(_remainingTicks > 0) --_remainingTicks;
        boolean skipUpdateBlocks = _lastPlaceLocation != BlockPos.ORIGIN && placeMode.getMode() == 1;

        ArrayList<BlockPos> placeLocations = new ArrayList<BlockPos>();
        EntityPlayer playerTarget = null;
        if (!skipUpdateBlocks && _remainingTicks <= 0)
        {
            _remainingTicks = ticks.getValue().intValue();
            final List<BlockPos> cachedCrystalBlocks = CrystalUtils.findCrystalBlocks(mc.player, placeRadius.getValue().floatValue()).stream().filter(pos -> verifyCrystalBlocks(pos)).collect(Collectors.toList());
            if (!cachedCrystalBlocks.isEmpty()) {
                float damage = 0f;
                String target = null;
                for (EntityPlayer player : mc.world.playerEntities) {
                    if (player == mc.player || MoodlyMod.friendsUtil.isFriend(player.getName()) || mc.player.isDead || (mc.player.getHealth() + mc.player.getAbsorptionAmount()) <= 0.0f)
                        continue;
                    float minDamage = minDmg.getValue().floatValue();
                    if (player.getHealth() + player.getAbsorptionAmount() <= facePlace.getValue())
                        minDamage = 1f;
                    for (BlockPos pos : cachedCrystalBlocks) {
                        float calculatedDamage = CrystalUtils.calculateDamage(mc.world, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, player, 0);
                        if (calculatedDamage >= minDamage && calculatedDamage > damage)
                        {
                            damage = calculatedDamage;
                            if (!placeLocations.contains(pos))
                                placeLocations.add(pos);
                            target = player.getName();
                            playerTarget = player;
                        }
                    }
                }
                if (playerTarget != null) {
                    if (playerTarget.isDead || playerTarget.getHealth() <= 0.0f) return;
                    if (!placeLocations.isEmpty())
                    {
                        float minDamage = minDmg.getValue().floatValue();
                        if (playerTarget.getHealth() + playerTarget.getAbsorptionAmount() <= facePlace.getValue())
                            minDamage = 1f;
                        final float finalMinDamage = minDamage;
                        final EntityPlayer finalTarget = playerTarget;
                        placeLocations.removeIf(pos -> CrystalUtils.calculateDamage(mc.world, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, finalTarget, 0) < finalMinDamage);

                        Collections.reverse(placeLocations);

                        _lastTarget = target;
                    }
                }
            }
        }

        EntityEnderCrystal crystal = getNearestCrystalTo(mc.player);
        boolean isValidCrystal = crystal != null ? mc.player.getDistance(crystal) < breakRadius.getValue() : false;
        if (!isValidCrystal && placeLocations.isEmpty() && !skipUpdateBlocks) {
            _remainingTicks = 0;
            return;
        }

        if (isValidCrystal && (skipUpdateBlocks ? true : _remainingTicks == ticks.getValue())) {
            if (antiWeakness.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                if (mc.player.getHeldItemMainhand() == ItemStack.EMPTY || (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) && !(mc.player.getHeldItemMainhand().getItem() instanceof ItemTool))) {
                    for (int i = 0; i < 9; ++i) {
                        ItemStack stack = mc.player.inventory.getStackInSlot(i);
                        if (stack.isEmpty())
                            continue;
                        if (stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemSword) {
                            mc.player.inventory.currentItem = i;
                            mc.playerController.updateController();
                            break;
                        }
                    }
                }
            }
            _rotations = EntityUtil.calculateLookAt(crystal.posX + 0.5, crystal.posY - 0.5, crystal.posZ + 0.5, mc.player);
            _rotationResetTimer.reset();

            mc.playerController.attackEntity(mc.player, crystal);
            mc.player.swingArm(EnumHand.MAIN_HAND);
            addAttackedCrystal(crystal);

            if (!multiPlace.getValue())
                return;
        }

        if (!placeLocations.isEmpty() || skipUpdateBlocks) {
            if (autoSwitch.getValue()) {
                if (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                    if (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL) {
                        for (int i = 0; i < 9; ++i) {
                            ItemStack stack = mc.player.inventory.getStackInSlot(i);
                            if (!stack.isEmpty() && stack.getItem() == Items.END_CRYSTAL) {
                                mc.player.inventory.currentItem = i;
                                mc.playerController.updateController();
                                break;
                            }
                        }
                    }
                }
            }
            if (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL)
                return;
            BlockPos selectedPos = null;
            if (!skipUpdateBlocks) {
                for (BlockPos pos : placeLocations) {
                    if (CrystalUtils.canPlaceCrystal(pos)) {
                        selectedPos = pos;
                        break;
                    }
                }
            }
            else selectedPos = _lastPlaceLocation;
            if (selectedPos == null) {
                _remainingTicks = 0;
                return;
            }
            _rotations = EntityUtil.calculateLookAt(selectedPos.getX() + 0.5, selectedPos.getY() - 0.5, selectedPos.getZ() + 0.5, mc.player);
            _rotationResetTimer.reset();

            RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(selectedPos.getX() + 0.5, selectedPos.getY() - 0.5, selectedPos.getZ() + 0.5));

            EnumFacing facing;

            if (result == null || result.sideHit == null)
                facing = EnumFacing.UP;
            else
                facing = result.sideHit;

            mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(selectedPos, facing,
                    mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0, 0, 0));

            if (_placedCrystals.contains(selectedPos))
                _placedCrystals.remove(selectedPos);
            _placedCrystals.add(selectedPos);

            if (playerTarget != null) {
                float calculatedDamage = CrystalUtils.calculateDamage(mc.world, selectedPos.getX() + 0.5, selectedPos.getY() + 1.0, selectedPos.getZ() + 0.5, playerTarget, 0);

                _placedCrystalsDamage.put(selectedPos, calculatedDamage);
            }

            if (_lastPlaceLocation != BlockPos.ORIGIN && _lastPlaceLocation == selectedPos) {
                if (placeMode.getMode() == 1)
                    _remainingTicks = 0;
            }
            else _lastPlaceLocation = selectedPos;
        }
    });

    @EventHandler
    private Listener<PlayerMotionUpdateEvent> playerMotionUpdateEventListener = new Listener<>(event -> {
        if(event.getEra() != CustomEvent.Era.PRE) return;
        if(event.isCancelled()) {
            _rotations = null;
            return;
        }
        if(needPause()) {
            _rotations = null;
            return;
        }
        if (_rotationResetTimer.passed(1000)) _rotations = null;
        if (_rotations != null)
        {
            event.cancel();
            PlayerUtil.packetFacePitchAndYaw((float)_rotations[1], (float)_rotations[0]);
        }
    });

    @EventHandler
    private Listener<NetworkPacketEvent> networkPacketEventListener = new Listener<>(event -> {
        if (event.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
            if (mc.world == null) return;
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                new ArrayList<Entity>(mc.world.loadedEntityList).forEach(e -> {
                    if (e instanceof EntityEnderCrystal)
                        if (e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0)
                            e.setDead();
                    _placedCrystals.removeIf(p_Pos -> p_Pos.getDistance((int)packet.getX(), (int)packet.getY(), (int)packet.getZ()) <= 6.0);
                });
            }
        }
    });

    @EventHandler
    private Listener<RenderEvent> renderEventListener = new Listener<>(event -> {
        if(mc.player == null) return;
        if (mc.getRenderManager() == null || !render.getValue())
            return;

        _placedCrystals.forEach(pos ->
        {
            final AxisAlignedBB bb = new AxisAlignedBB(pos.getX() - mc.getRenderManager().viewerPosX,
                    pos.getY() - mc.getRenderManager().viewerPosY, pos.getZ() - mc.getRenderManager().viewerPosZ,
                    pos.getX() + 1 - mc.getRenderManager().viewerPosX,
                    pos.getY() + (1) - mc.getRenderManager().viewerPosY,
                    pos.getZ() + 1 - mc.getRenderManager().viewerPosZ);

            camera.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY,
                    mc.getRenderViewEntity().posZ);

            if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + mc.getRenderManager().viewerPosX,
                    bb.minY + mc.getRenderManager().viewerPosY, bb.minZ + mc.getRenderManager().viewerPosZ,
                    bb.maxX + mc.getRenderManager().viewerPosX, bb.maxY + mc.getRenderManager().viewerPosY,
                    bb.maxZ + mc.getRenderManager().viewerPosZ)))
            {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.disableDepth();
                GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask(false);
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
                GL11.glLineWidth(1.5f);

                int color = (alpha.getValue().intValue() << 24) | (red.getValue().intValue() << 16) | (green.getValue().intValue() << 8) | blue.getValue().intValue();

                RenderUtils.drawBoundingBox(bb, 1.0f, color);
                RenderUtils.drawFilledBox(bb, color);
                GL11.glDisable(GL11.GL_LINE_SMOOTH);
                GlStateManager.depthMask(true);
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();

                if (_placedCrystalsDamage.containsKey(pos)) {
                    GlStateManager.pushMatrix();
                    RenderUtils.glBillboardDistanceScaled((float) pos.getX() + 0.5f, (float) pos.getY() + 0.5f, (float) pos.getZ() + 0.5f, mc.player, 1);
                    final float damage = _placedCrystalsDamage.get(pos);
                    final String damageText = (Math.floor(damage) == damage ? (int) damage : String.format("%.1f", damage)) + "";
                    GlStateManager.disableDepth();
                    GlStateManager.translate(-(Wrapper.getMC().fontRenderer.getStringWidth(damageText) / 2.0d), 0, 0);
                    Wrapper.getMC().fontRenderer.drawStringWithShadow(damageText, 0, 0, -1);
                    GlStateManager.popMatrix();
                }
            }
        });
    });

    public boolean needPause()
    {
        if(mc.player == null) return true;

        if (pauseIfHittingBlock.getValue() && mc.playerController.isHittingBlock && mc.player.getHeldItemMainhand().getItem() instanceof ItemTool)
            return true;
        return false;
    }
}