package us.np.moodlymod.command.commands;

import net.minecraft.util.text.TextComponentString;
import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.command.Command;
import us.np.moodlymod.command.CommandManager;
import us.np.moodlymod.util.ChatColor;

public class CommandPrefix extends Command {
    public CommandPrefix() {
        super("prefix");
    }

    @Override
    public void call(String[] args) {
        super.call(args);

        if(args.length < 1) {
            mc.player.sendMessage(new TextComponentString(ChatColor.parse("&", "&c" + CommandManager.prefix + getAlias() + " <prefix>")));
            return;
        }
        MoodlyMod.commandManager.setPrefix(args[0]);
        mc.player.sendMessage(new TextComponentString(ChatColor.parse("&", "&aPrefix changed to &e" + args[0])));
    }
}