package us.np.moodlymod.module;

import static us.np.moodlymod.MoodlyMod.debug;

import me.zero.alpine.fork.listener.Listenable;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.Wrapper;
import us.np.moodlymod.module.option.*;
import us.np.moodlymod.util.ConfigUtils;

import java.awt.*;
import java.util.ArrayList;

public class Module implements Listenable {
    public String displayName;
    private String[] alias;
    private String desc;
    public String key;
    private Color color;
    private boolean enabled = false;
    public boolean showInArray = true;
    public ModuleType moduleType;
    private boolean m_NeedsClickGuiValueUpdate;
    public static Minecraft mc;

    private ConfigUtils configUtils;

    private ArrayList<Option> options;

    public Module(String displayName, String[] alias, String key, Color color, ModuleType moduleType) {
        mc = Wrapper.getMC();
        this.displayName = displayName;
        this.alias = alias;
        this.key = key;
        this.color = color;
        this.moduleType = moduleType;
        this.options = new ArrayList<Option>();
        this.configUtils = new ConfigUtils(displayName, "modules");

        if(this.moduleType == ModuleType.ALWAYS_ENABLED) {
            this.enabled = true;
            this.key = "NONE";
            this.showInArray = false;
        }
    }

    public Module(String displayName, String[] alias, String desc, String key, Color color, ModuleType type) {
        this(displayName, alias, key, color, type);
        this.desc = desc;
    }

    public void save() {
        debug(displayName, "Saving JSON");
        configUtils.set("enabled", enabled);
        configUtils.set("key", key);
        debug(displayName, "Set defaults");
        for(Option option : getOptions()) {
            debug(displayName, "Option " + option.getName() + " saving!");
            if (option instanceof OptionBetterMode)
                configUtils.set(option.getName(), ((OptionBetterMode) option).getMode());
            else if(option instanceof OptionDouble)
                configUtils.set(option.getName(), ((OptionDouble)option).getValue());
            else
                configUtils.set(option.getName(), option.getValue());
        }
        configUtils.save();
    }

    public void onEnable() {
        MoodlyMod.EVENT_BUS.subscribe(this);
    }
    public void onDisable() {
        MoodlyMod.EVENT_BUS.unsubscribe(this);
    }
    public boolean isEnabled() {return enabled;}
    public void addOption(Option option) { debug(displayName, "Option add!"); if(!options.contains(option)) options.add(option);}
    public void addOption(Option option, OptionBetterMode mode, int modeId) {
        option.configShowMode(mode, modeId);
        if(!options.contains(option))
            options.add(option);
        debug(displayName, "Option add!");
    }
    public void endOption() {
        debug(displayName, "Cached options!");
        if(configUtils.getJSON().isEmpty()) {
            debug(displayName, "JSON Empty!");
            save();
        } else {
            ArrayList<Object> arrayList = new ArrayList<Object>();
            arrayList.add(configUtils.get("enabled"));
            arrayList.add(configUtils.get("key"));
            for(Option option : getOptions())
                arrayList.add(configUtils.get(option.getName()));
            boolean succ = false;
            for(Object object : arrayList)
                if(object == null)
                    succ = true;
            if(succ) save();
            arrayList.clear();

            enabled = (boolean)configUtils.get("enabled");
            key = (String)configUtils.get("key");
            debug(displayName, "Set defaults! [" + enabled + ", " + key + "]");
            for(Option option : getOptions()){
                if(option instanceof OptionDouble) {
                    double a = configUtils.getJSON().getDouble(option.getName());
                    ((OptionDouble) option).setValue(a);
                    debug(displayName, ((OptionDouble)option).getName() + " option set to " + a);
                }
                else if(option instanceof OptionBetterMode) {
                    int a = configUtils.getJSON().getInt(option.getName());
                    ((OptionBetterMode) option).setMode(a);
                    debug(displayName, ((OptionBetterMode)option).getName() + " option set to " + a);
                }
                else {
                    Object a = configUtils.get(option.getName());
                    option.setValue(a);
                    debug(displayName, option.getName() + " option set to " + a);
                }
            }
        }

        if(enabled) onEnable();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public void toggle(boolean save) {
        setEnabled(!enabled);
        if(enabled) onEnable(); else onDisable();

        // if(save) save();
    }

    public void setAlias(String[] alias) {this.alias = alias;}
    public void setDesc(String desc) {this.desc = desc;}
    public void setColor(Color color) {this.color = color;}
    public String[] getAlias() { return alias; }
    public String getDesc() {return desc;}
    public Color getColor() {return color;}
    public ArrayList<Option> getOptions() { return options; }
    public ModuleType getModuleType() {return moduleType;}
}