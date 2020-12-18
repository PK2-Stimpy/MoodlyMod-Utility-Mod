package us.np.moodlymod.module.modules.movement;

import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.BetterMode;
import us.np.moodlymod.module.option.Option;
import us.np.moodlymod.module.option.OptionBetterMode;

import java.awt.*;

public class StepModule extends Module {
    public static final OptionBetterMode mode = new OptionBetterMode("Mode", 0 , BetterMode.construct("Normal", "Reverse"));



    public StepModule(){
        super("Step", null, "NONE", Color.CYAN, ModuleType.MOVEMENT);
        addOption(mode);
        endOption();
    }
}