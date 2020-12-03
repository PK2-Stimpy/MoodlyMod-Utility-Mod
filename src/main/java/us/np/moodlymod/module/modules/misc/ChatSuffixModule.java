package us.np.moodlymod.module.modules.misc;

import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.command.commands.CommandChatSuffix;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;

import java.awt.*;

public class ChatSuffixModule extends Module {
    public ChatSuffixModule() {
        super("ChatSuffix", null, "NONE", Color.BLUE, ModuleType.MISC);
        endOption();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        ChatModule chatModule = (ChatModule) MoodlyMod.moduleManager.getModuleByClass(ChatModule.class);
        if(!chatModule.isEnabled())
            chatModule.toggle(true);
    }

    public String getSuffix() { return CommandChatSuffix.INSTANCE.getSuffix(); }
}