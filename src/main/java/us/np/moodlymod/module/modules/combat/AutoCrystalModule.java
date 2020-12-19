package us.np.moodlymod.module.modules.combat;

import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.BetterMode;
import us.np.moodlymod.module.option.OptionBetterMode;
import us.np.moodlymod.module.option.OptionBoolean;
import us.np.moodlymod.module.option.OptionDouble;

import java.awt.*;

public class AutoCrystalModule extends Module {
    public static final OptionBetterMode setting = new OptionBetterMode("Setting", 0, BetterMode.construct("PLACE", "BREAK", "RENDER", "MISC", "DEV"));
    public static final OptionBetterMode raytrace = new OptionBetterMode("Raytrace", 0, BetterMode.construct("NONE", "PLACE", "BREAK", "FULL"));
    //public stati                                                          c final OptionBoolean place = new OptionBoolean("Place", true);
    public static final OptionDouble placeDelay = new OptionDouble("Place delay", 12D, 0D, 1000D);
    //pu

    public AutoCrystalModule() {
        super("AutoCrystal", null, "NONE", Color.RED, ModuleType.COMBAT);
    }
}