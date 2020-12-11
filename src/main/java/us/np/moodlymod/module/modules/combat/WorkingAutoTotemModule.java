package us.np.moodlymod.module.modules.combat;

import com.maxwell.kmeth.gui.GUI;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import net.minecraft.util.text.TextComponentString;
import us.np.moodlymod.event.custom.entityplayersp.PlayerUpdateEvent;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.BetterMode;
import us.np.moodlymod.module.option.OptionBetterMode;
import us.np.moodlymod.module.option.OptionBoolean;
import us.np.moodlymod.module.option.OptionDouble;
import us.np.moodlymod.util.ChatColor;
import us.np.moodlymod.util.PlayerUtil;

import java.awt.*;

public class WorkingAutoTotemModule extends Module {
    public static final OptionDouble health = new OptionDouble("Health", 13.1D, 0D, 20D);
    public static final OptionBetterMode mode = new OptionBetterMode("Mode", 2, BetterMode.construct("Totem", "Gap", "Crystal", "Pearl", "Chorus", "Strength"));
    public static final OptionBetterMode fallbackMode = new OptionBetterMode("FallbackMode", 2, BetterMode.construct("Totem", "Gap", "Crystal", "Pearl", "Chorus", "Strength"));
    public static final OptionDouble fallDistance = new OptionDouble("Fall distance", 15D, 0D, 100D);
    public static final OptionBoolean totemOnElytra = new OptionBoolean("Totem on elytra", true);
    public static final OptionBoolean offhandGapOnSword = new OptionBoolean("SwordGap", true);
    public static final OptionBoolean offhandStrNoStrSword = new OptionBoolean("StrGap", false);
    public static final OptionBoolean hotbarFirst = new OptionBoolean("Hotbar first", false);

    public static WorkingAutoTotemModule INSTANCE;

    public WorkingAutoTotemModule() {
        super("AutoTotem", null, "NONE", Color.RED, ModuleType.COMBAT);
        addOption(health);
        addOption(mode);
        addOption(fallbackMode);
        addOption(fallDistance);
        addOption(totemOnElytra);
        addOption(offhandGapOnSword);
        addOption(offhandStrNoStrSword);
        addOption(hotbarFirst);
        endOption();

        INSTANCE = this;
    }

    @Override
    public String getMeta() { return mode.getDisplayName(); }

    private void switchOffHandIfNeed(int iMode) {
        Item item = getItemFromModeVal(iMode);
        if (mc.player.getHeldItemOffhand().getItem() != item) {
            int slot = hotbarFirst.getValue() ? PlayerUtil.getRecursiveItemSlot(item) : PlayerUtil.getItemSlot(item);
            Item fallback = getItemFromModeVal(fallbackMode.getMode());
            String display = getItemNameFromModeVal(iMode);

            if (slot == -1 && item != fallback && mc.player.getHeldItemOffhand().getItem() != fallback) {
                slot = PlayerUtil.getRecursiveItemSlot(fallback);
                display = getItemNameFromModeVal(fallbackMode.getMode());

                if (slot == -1 && fallback != Items.TOTEM_OF_UNDYING) {
                    fallback = Items.TOTEM_OF_UNDYING;
                    if (item != fallback && mc.player.getHeldItemOffhand().getItem() != fallback) {
                        slot = PlayerUtil.getRecursiveItemSlot(fallback);
                        display = "Emergency Totem";
                    }
                }
            }
            if (slot != -1) {
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0,
                        ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP,
                        mc.player);

                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0,
                        ClickType.PICKUP, mc.player);
                mc.playerController.updateController();

                mc.player.sendMessage(new TextComponentString(ChatColor.parse("&", "&e[AutoTotem] &bOffhand has now a " + display + " &c(May desync)")));
            }
        }
    }

    public Item getItemFromModeVal(int iMode) {
        switch (iMode) {
            case 2:
                return Items.END_CRYSTAL;
            case 1:
                return Items.GOLDEN_APPLE;
            case 3:
                return Items.ENDER_PEARL;
            case 4:
                return Items.CHORUS_FRUIT;
            case 5:
                return Items.POTIONITEM;
            default:
                break;
        }
        return Items.TOTEM_OF_UNDYING;
    }

    private String getItemNameFromModeVal(int iMode) {
        if(iMode == 2) return "End Crystal";
        return mode.getModes()[iMode].mode;
    }

    @EventHandler
    private Listener<PlayerUpdateEvent> updateEventListener = new Listener<>(event -> {
        if (mc.currentScreen != null && (!(mc.currentScreen instanceof GuiInventory) && !(mc.currentScreen instanceof GUI)))
            return;

        float healthF = (mc.player.getHealth() + mc.player.getAbsorptionAmount());
        if (!mc.player.getHeldItemMainhand().isEmpty()) {
            if (health.getValue() <= healthF && mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && offhandStrNoStrSword.getValue() && !mc.player.isPotionActive(MobEffects.STRENGTH)) {
                switchOffHandIfNeed(5);
                return;
            }
            if (health.getValue() <= healthF && mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && offhandGapOnSword.getValue()) {
                switchOffHandIfNeed(1);
                return;
            }
        }

        if (health.getValue() > healthF || mode.getMode() == 0 || (totemOnElytra.getValue() && mc.player.isElytraFlying()) || (mc.player.fallDistance >= fallDistance.getValue() && !mc.player.isElytraFlying())) {
            switchOffHandIfNeed(0);
            return;
        }
        switchOffHandIfNeed(mode.getMode());
    });
}