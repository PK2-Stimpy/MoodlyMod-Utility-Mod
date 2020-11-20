package us.np.moodlymod.module.modules.ui;

import net.minecraft.util.text.TextComponentString;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.BetterMode;
import us.np.moodlymod.module.option.OptionBetterMode;

import java.awt.*;

public class TestBetterModeModule extends Module {
    public static final OptionBetterMode testOption = new OptionBetterMode("Mode", 0, BetterMode.construct("YES", "YES2", "YES3", "ATTACK", "SYN", "LOL"));
    public static final OptionBetterMode testOption2 = new OptionBetterMode("Mode2", 0, BetterMode.construct("SYNAPSE"));

    public TestBetterModeModule() {
        super("BetterModeTest", null, "NONE", Color.CYAN, ModuleType.UI);
        addOption(testOption);
        addOption(testOption2);
        endOption();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        mc.player.sendMessage(new TextComponentString(testOption.getName() + ": " + testOption.getDisplayName()));
        mc.player.sendMessage(new TextComponentString(testOption2.getName() + ": " + testOption2.getDisplayName()));
    }

    @Override
    public void onDisable() {
        super.onDisable();

        mc.player.sendMessage(new TextComponentString(testOption.getName() + ": " + testOption.getDisplayName()));
        mc.player.sendMessage(new TextComponentString(testOption2.getName() + ": " + testOption2.getDisplayName()));
    }
}