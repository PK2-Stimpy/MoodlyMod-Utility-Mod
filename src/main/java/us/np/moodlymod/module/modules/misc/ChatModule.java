package us.np.moodlymod.module.modules.misc;

import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;
import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.command.CommandManager;
import us.np.moodlymod.event.custom.entityplayersp.PlayerChatEvent;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;

import java.awt.*;

public class ChatModule extends Module {
    public String[] exceptions = {"/",".",";"};
    public ChatSuffixModule chatSuffixModule;
    public AnarchyColoredChatModule coloredChatModule;

    public ChatModule() {
        super("ChatModule", null, "NONE", Color.BLUE, ModuleType.MISC);
        endOption();
    }

    @EventHandler
    private Listener<PlayerChatEvent> playerChatEventListener = new Listener<>(event -> {
        for(String exception : exceptions)
            if(event.message.startsWith(exception))
                return;
        if(event.message.startsWith(CommandManager.prefix))
            return;

        event.cancel();

        String message = event.message;

        chatSuffixModule = (ChatSuffixModule) MoodlyMod.moduleManager.getModuleByClass(ChatSuffixModule.class);
        if(chatSuffixModule.isEnabled())
            message+=(" >> "+chatSuffixModule.getSuffix());
        coloredChatModule = (AnarchyColoredChatModule) MoodlyMod.moduleManager.getModuleByClass(AnarchyColoredChatModule.class);
        if(coloredChatModule.isEnabled())
            message = coloredChatModule.getColorString() + message;

        mc.player.connection.sendPacket(new CPacketChatMessage(message));
    });
}