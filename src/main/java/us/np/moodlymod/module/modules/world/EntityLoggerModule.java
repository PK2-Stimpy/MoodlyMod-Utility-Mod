package us.np.moodlymod.module.modules.world;

import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.util.text.TextComponentString;
import us.np.moodlymod.event.custom.world.EntityAddedEvent;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.Option;
import us.np.moodlymod.module.option.OptionBoolean;
import us.np.moodlymod.util.ChatColor;

import java.awt.*;

public class EntityLoggerModule extends Module {
    public static final OptionBoolean donkey = new OptionBoolean("Donkeys", true);
    public static final OptionBoolean llama = new OptionBoolean("Llamas", true);
    public static final OptionBoolean slime = new OptionBoolean("Slimes", false);

    public EntityLoggerModule() {
        super("EntityLogger", new String[] {""}, "NONE", Color.getHSBColor(23,69,64), ModuleType.WORLD);
        addOption(donkey);
        addOption(llama);
        addOption(slime);
        endOption();
    }

    @EventHandler
    private Listener<EntityAddedEvent> entityAddedEventListener = new Listener<>(event -> {
        if(mc.player == null) return;

        Entity entity = event.getEntity();
        if((entity instanceof EntityLlama) && llama.getValue()) {
            EntityLlama llama = (EntityLlama)entity;
            mc.player.sendMessage(new TextComponentString(ChatColor.AQUA.s() + String.format("[MOODLYMOD] Found llama at [%s, %s]", String.valueOf(llama.posX), String.valueOf(llama.posZ))));
        } else if((entity instanceof EntityDonkey) && donkey.getValue()) {
            EntityDonkey donkey = (EntityDonkey)entity;
            mc.player.sendMessage(new TextComponentString(ChatColor.AQUA.s() + String.format("[MOODLYMOD] Found donkey at [%s, %s]", String.valueOf(donkey.posX), String.valueOf(donkey.posZ))));
        } else if((entity instanceof EntitySlime) && slime.getValue()) {
            EntitySlime entitySlime = (EntitySlime)entity;
            mc.player.sendMessage(new TextComponentString(ChatColor.AQUA.s() + String.format("[MOODLYMOD] Found slime at [%s, %s]", String.valueOf(entitySlime.posX), String.valueOf(entitySlime.posZ))));
        }
    });
}
