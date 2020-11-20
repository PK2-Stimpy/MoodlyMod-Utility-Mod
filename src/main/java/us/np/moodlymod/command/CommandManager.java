package us.np.moodlymod.command;

import org.apache.commons.lang3.ArrayUtils;
import us.np.moodlymod.command.commands.CommandFriend;
import us.np.moodlymod.command.commands.CommandPrefix;
import us.np.moodlymod.util.ConfigUtils;

import java.util.ArrayList;
import static us.np.moodlymod.command.CommandReturnStatus.*;

public class CommandManager {
    public static String prefix = ".";

    protected final ConfigUtils configUtils;
    private ArrayList<Command> commands;

    public CommandManager() {
        configUtils = new ConfigUtils("config", "commands");
        Object object = configUtils.get("prefix");
        if(object == null) configUtils.set("prefix", prefix);
        else prefix = (String)object;

        commands = new ArrayList<Command>();
        commands.add(new CommandPrefix());
        commands.add(new CommandFriend());
    }

    public Command get(String alias) {
        for(Command command : commands)
            if(command.getAlias().equalsIgnoreCase(alias))
                return command;
        return null;
    }
    public CommandReturnStatus gotMessage(String message) {
        if(!message.startsWith(prefix)) return COMMAND_INVALID_SYNTAX;
        String[] args = message.split(" ");
        args[0] = args[0].replaceFirst(prefix, "");
        Command command = get(args[0]);
        if(command == null) return COMMAND_INVALID;
        args = ArrayUtils.remove(args, 0);
        command.call(args);
        return COMMAND_VALID;
    }

    public ConfigUtils getConfigUtils() { return configUtils; }

    public void setPrefix(String newPrefix) {
        CommandManager.prefix = newPrefix;
        configUtils.set("prefix", CommandManager.prefix);
        configUtils.save();
    }
}