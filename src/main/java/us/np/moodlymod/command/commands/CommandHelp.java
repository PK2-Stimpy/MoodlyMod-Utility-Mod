package us.np.moodlymod.command.commands;

import net.minecraft.util.text.TextComponentString;
import us.np.moodlymod.command.Command;
import us.np.moodlymod.util.ChatColor;

public class CommandHelp extends Command {
    public CommandHelp() {
        super("help");
    }

    @Override
    public void call(String[] args) {
        super.call(args);
        mc.player.sendMessage(new TextComponentString(ChatColor.parse("&", "&e" +
                "Commands: \n&b" +
                "   " + formatCmd("help") + "\n&b" +
                "   " + formatCmd("friend") + "\n&b" +
                "   " + formatCmd("prefix") + "\n&b" +
                "   " + formatCmd("chat-suffix") + "\n&r "
        )));
    }
}