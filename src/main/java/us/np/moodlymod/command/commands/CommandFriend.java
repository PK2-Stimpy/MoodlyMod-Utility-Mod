package us.np.moodlymod.command.commands;

import net.minecraft.util.text.TextComponentString;
import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.command.Command;
import us.np.moodlymod.command.CommandManager;
import us.np.moodlymod.util.ChatColor;

public class CommandFriend extends Command {
    public CommandFriend() { super("friend"); }

    @Override
    public void call(String[] args) {
        super.call(args);

        if(args.length >= 1) {
            if(args[0].equalsIgnoreCase("list")) {
                mc.player.sendMessage(new TextComponentString(ChatColor.parse("&", "&bFriends:")));
                for(Object object : MoodlyMod.friendsUtil.friends.toList())
                    if(object instanceof String)
                        mc.player.sendMessage(new TextComponentString(ChatColor.parse("&", "  &e" + (String)object)));
                return;
            }
            if(args[0].equalsIgnoreCase("add")) {
                if(args.length < 2) {
                    mc.player.sendMessage(new TextComponentString(ChatColor.RED + formatCmd("friend add <player>")));
                    return;
                }
                MoodlyMod.friendsUtil.addFriend(args[1]);
                mc.player.sendMessage(new TextComponentString(ChatColor.GREEN + "Added " + args[1] + " to the friends list!"));
                return;
            }
            if(args[0].equalsIgnoreCase("rem")) {
                if(args.length < 2) {
                    mc.player.sendMessage(new TextComponentString(ChatColor.RED + formatCmd("friend add <player>")));
                    return;
                }
                MoodlyMod.friendsUtil.remFriend(args[1]);
                mc.player.sendMessage(new TextComponentString(ChatColor.YELLOW + "Removed " + args[1] + " from the friends list!"));
                return;
            }
        }

        mc.player.sendMessage(new TextComponentString(ChatColor.parse("&", "\n&c   " +
                formatCmd("friend add <player>") + "\n&c   " +
                formatCmd("friend rem <player>") + "\n&c   " +
                formatCmd("friend list" + "\n"))));
    }
}