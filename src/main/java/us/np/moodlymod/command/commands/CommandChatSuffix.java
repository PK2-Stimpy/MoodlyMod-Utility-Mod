package us.np.moodlymod.command.commands;

import net.minecraft.util.text.TextComponentString;
import us.np.moodlymod.command.Command;
import us.np.moodlymod.util.ChatColor;
import us.np.moodlymod.util.ConfigUtils;

public class CommandChatSuffix extends Command {
    public static CommandChatSuffix INSTANCE;

    public ConfigUtils configUtils;
    private String suffix = "Moodly";

    public CommandChatSuffix() {
        super("chat-suffix");
        CommandChatSuffix.INSTANCE = this;
        configUtils = new ConfigUtils("chat", "additional-config");
        if(configUtils.get("suffix") == null) {
            configUtils.set("suffix", suffix);
            configUtils.save();
        } else suffix = (String)configUtils.get("suffix");
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
        configUtils.set("suffix", suffix);
        configUtils.save();
    }

    public String getSuffix() { return suffix; }

    @Override
    public void call(String[] args) {
        super.call(args);
        if(args.length < 1) {
            mc.player.sendMessage(new TextComponentString(ChatColor.YELLOW + "Your suffix is: " + ChatColor.GREEN + suffix));
            return;
        }
        setSuffix(args[0]);
        mc.player.sendMessage(new TextComponentString(ChatColor.YELLOW + "Your new suffix is: " + ChatColor.GREEN + suffix));
    }
}