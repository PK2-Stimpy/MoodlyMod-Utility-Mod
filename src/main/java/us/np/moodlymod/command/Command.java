package us.np.moodlymod.command;

import net.minecraft.client.Minecraft;

public class Command {
    protected final Minecraft mc;
    private String alias;

    public Command(String alias) {
        mc = Minecraft.getMinecraft();
        this.alias = alias;
    }

    public String formatCmd(String cmd) { return CommandManager.prefix + cmd; }

    public String getAlias() { return alias; }

    public void call(String[] args) {}
}