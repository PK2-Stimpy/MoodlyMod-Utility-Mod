package us.np.moodlymod.module.modules.misc;

import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import org.lwjgl.input.Keyboard;
import us.np.moodlymod.event.custom.entityplayersp.PlayerUpdateEvent;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.OptionBoolean;
import us.np.moodlymod.module.option.OptionDouble;
import us.np.moodlymod.util.ChatColor;
import us.np.moodlymod.util.Timer;

import java.awt.*;
import java.util.Comparator;

public class AutoSalDupeModule extends Module {
    public static final OptionBoolean shulkerOnly = new OptionBoolean("Dupe only shulkers", true);
    public static final OptionBoolean hitGround = new OptionBoolean("Hit ground", true);
    public static final OptionDouble delay = new OptionDouble("Delay", 1D, 0D, 10D);

    private boolean doDrop = false;
    private boolean doChest = false;
    private boolean doSneak = false;
    private boolean start = false;
    private boolean finished = false;
    private boolean grounded = false;

    private int itemsToDupe;
    private int itemsMoved;
    private int itemsDropped;

    private GuiScreenHorseInventory horseInventory;
    private final Timer timer = new Timer();

    private boolean noBypass = false;

    public AutoSalDupeModule() {
        super("AutoSalDupe", null, "NONE", Color.YELLOW, ModuleType.MISC);
        addOption(shulkerOnly);
        addOption(hitGround);
        addOption(delay);
        endOption();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        timer.reset();
        start = true;

        mc.player.sendMessage(new TextComponentString(ChatColor.parse("&", "&eAutoDupe enabled, press &lESC &ekey to disable.")));
    }

    @Override
    public void onDisable() {
        super.onDisable();

        noBypass = false;
        doDrop = false;
        doChest = false;
        doSneak = false;
        start = false;
        finished = false;
        grounded = false;
        itemsToDupe = 0;
        itemsMoved = 0;
        itemsDropped = 0;
        timer.reset();
    }

    private boolean isValidEntity(Entity entity) {
        if (entity instanceof AbstractChestHorse) {
            AbstractChestHorse chestHorse = (AbstractChestHorse)entity;
            return !chestHorse.isChild() && chestHorse.isTame();
        }
        return false;
    }

    private int getChestInHotbar() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                final Block block = ((ItemBlock) stack.getItem()).getBlock();
                if (block instanceof BlockChest)
                    return i;
            }
        }
        return -1;
    }

    private void handleStoring(int p_WindowId, int p_Slot) {
        for (int y = 9; y < mc.player.inventoryContainer.inventorySlots.size() - 1; ++y) {
            ItemStack l_InvStack = mc.player.inventoryContainer.getSlot(y).getStack();
            if (l_InvStack.isEmpty() || l_InvStack.getItem() == Items.AIR)
                continue;
            if (!(l_InvStack.getItem() instanceof ItemShulkerBox) && shulkerOnly.getValue())
                continue;

            mc.playerController.windowClick(p_WindowId, y + p_Slot, 0, ClickType.QUICK_MOVE, mc.player);
            return;
        }
    }

    private void doDupe() {
        noBypass = true;

        Entity entity = mc.world.loadedEntityList.stream()
                .filter(this::isValidEntity)
                .min(Comparator.comparing(p_Entity -> mc.player.getDistance(p_Entity)))
                .orElse(null);
        if (entity instanceof AbstractChestHorse) {
            mc.player.connection.sendPacket(new CPacketUseEntity(entity, EnumHand.MAIN_HAND, entity.getPositionVector()));
            noBypass = false;
            doDrop = true;
        }
    }

    private int getItemsToDupe() {
        int i = 0;
        for (int y = 9; y < mc.player.inventoryContainer.inventorySlots.size() - 1; ++y) {
            ItemStack itemStack = mc.player.inventoryContainer.getSlot(y).getStack();
            if (itemStack.isEmpty() || itemStack.getItem() == Items.AIR)
                continue;
            if (!(itemStack.getItem() instanceof ItemShulkerBox) && shulkerOnly.getValue())
                continue;

            i++;
        }

        if(i > horseInventory.horseInventory.getSizeInventory() - 1)
            i = horseInventory.horseInventory.getSizeInventory() - 1;
        return i;
    }

    private int getItemsInRidingEntity() {
        int i = 0;

        for(int y = 2; y < horseInventory.horseInventory.getSizeInventory() + 1; ++y) {
            ItemStack l_ItemStack = horseInventory.horseInventory.getStackInSlot(y);
            if(l_ItemStack.isEmpty() || l_ItemStack.getItem() == Items.AIR)
                continue;

            i++;
        }
        return i;
    }

    private boolean canStore() {
        for (int l_Y = 9; l_Y < mc.player.inventoryContainer.inventorySlots.size() - 1; ++l_Y) {
            ItemStack l_InvStack = mc.player.inventoryContainer.getSlot(l_Y).getStack();
            return (l_InvStack.isEmpty() || l_InvStack.getItem() == Items.AIR);
        }
        return false;
    }

    public boolean ignoreMountBypass() { return noBypass; }


    @EventHandler
    private Listener<PlayerUpdateEvent> updateEventListener = new Listener<>(event -> {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            toggle(true);
            return;
        }
        if(finished) {
            finished = false;
            itemsMoved = 0;
            itemsDropped = 0;
            start = true;
            return;
        }
        if (!timer.passed(delay.getValue() * 100f))
            return;
        timer.reset();

        if (doSneak) {
            if(!mc.player.isSneaking()) {
                mc.gameSettings.keyBindSneak.pressed = true;
                return;
            }
            mc.gameSettings.keyBindSneak.pressed = false;
            doSneak = false;
            if(!hitGround.getValue())
                finished = true;
            else grounded = true;

            return;
        }

        if (start && isEnabled()) {
            itemsToDupe = 0;
            itemsMoved = 0;

            Entity entity = mc.world.loadedEntityList.stream()
                    .filter(this::isValidEntity)
                    .min(Comparator.comparing(entity1 -> mc.player.getDistance(entity1)))
                    .orElse(null);
            if (entity instanceof AbstractChestHorse) {
                AbstractChestHorse horse = (AbstractChestHorse) entity;
                if(horse.hasChest()) {
                    int slot = getChestInHotbar();
                    if (slot != -1 && mc.player.inventory.currentItem != slot) {
                        mc.player.inventory.currentItem = slot;
                        mc.playerController.updateController();
                        mc.playerController.interactWithEntity(mc.player, horse, EnumHand.MAIN_HAND);
                    } else if (mc.player.inventory.currentItem != slot) {
                        mc.player.sendMessage(new TextComponentString(ChatColor.parse("&", "&cYou don't have any chest in hotbar, toggling!")));
                        toggle(true);
                        return;
                    } else mc.playerController.interactWithEntity(mc.player, horse, EnumHand.MAIN_HAND);
                }

                start = false;
                mc.playerController.interactWithEntity(mc.player, horse, EnumHand.MAIN_HAND);
                mc.player.sendHorseInventory();
                doChest = true;
            }
        }
        if (doChest && !(mc.currentScreen instanceof GuiScreenHorseInventory)) {
            doChest = false;
            start = true;
            return;
        }

        if (mc.currentScreen instanceof GuiScreenHorseInventory) {
            horseInventory = (GuiScreenHorseInventory) mc.currentScreen;
            itemsToDupe = getItemsToDupe();

            for (int y = 2; y < horseInventory.horseInventory.getSizeInventory() + 1; ++y) {
                ItemStack stack = horseInventory.horseInventory.getStackInSlot(y);
                if(((itemsToDupe == 0 || itemsMoved == horseInventory.horseInventory.getSizeInventory() - 2) && doChest) || ((itemsDropped >= itemsMoved) && doDrop))
                    break;

                if ((stack.isEmpty() || stack.getItem() == Items.AIR) && doChest) {
                    handleStoring(horseInventory.inventorySlots.windowId, horseInventory.horseInventory.getSizeInventory() - 9);
                    itemsToDupe--;
                    itemsMoved = getItemsInRidingEntity();
                    return;
                } else if(doChest) continue;

                if ((shulkerOnly.getValue() && !(stack.getItem() instanceof ItemShulkerBox)) || stack.isEmpty())
                    continue;

                if (doDrop) {
                    if(canStore())
                        mc.playerController.windowClick(mc.player.openContainer.windowId, y, 0, ClickType.QUICK_MOVE, mc.player);
                    else mc.playerController.windowClick(horseInventory.inventorySlots.windowId, y, -999, ClickType.THROW, mc.player);
                    itemsDropped++;

                    return;
                }
            }
            if (doChest) {
                doChest = false;
                doDupe();
                return;
            }
            if(doDrop) {
                doDrop = false;
                mc.player.closeScreen();
                mc.gameSettings.keyBindSneak.pressed = true;
                doSneak = true;
            }
        }
    });

    @EventHandler
    private Listener<EntityJoinWorldEvent> worldEventListener = new Listener<>(event -> {
        if (event.getEntity() == mc.player)
            toggle(true);
    });

    @Override
    public String getMeta() {
        return String.valueOf(delay.getValue());
    }
}