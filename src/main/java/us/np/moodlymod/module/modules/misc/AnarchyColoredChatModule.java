package us.np.moodlymod.module.modules.misc;

import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.BetterMode;
import us.np.moodlymod.module.option.OptionBetterMode;

import java.awt.*;

public class AnarchyColoredChatModule extends Module {
    public static final OptionBetterMode color = new OptionBetterMode("Color", 0, BetterMode.construct("GREEN", "BLUE", "YELLOW"));

    public AnarchyColoredChatModule() {
        super("AnarchyColoredChat", null, "NONE", Color.BLUE, ModuleType.MISC);
        addOption(color);
        endOption();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        ChatModule chatModule = (ChatModule) MoodlyMod.moduleManager.getModuleByClass(ChatModule.class);
        if(chatModule == null) return;
        if(!chatModule.isEnabled())
            chatModule.toggle(true);
    }

    public String getColorString() {
        switch (color.getMode()) {
            case 0: return ">";
            case 1: return "``";
            case 2: return "#";
            default: break;
        }
        return "";
    }

    @Override
    public String getMeta() {
        return color.getDisplayName();
    }
}