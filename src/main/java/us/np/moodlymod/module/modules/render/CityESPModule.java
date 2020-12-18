package us.np.moodlymod.module.modules.render;

import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.*;

import java.awt.*;

public class CityESPModule extends Module {
    public static final OptionBoolean endcrystal_mode = new OptionBoolean("EndCrystal", false);
    public static final OptionBetterMode mode = new OptionBetterMode("Mode", 0 , BetterMode.construct("Pretty", "Solid", "Outline"));
    public static final OptionDouble off_set = new OptionDouble("Height", 0.2D, 0.0D, 1.0D);
    public static final OptionDouble range = new OptionDouble("Range",  6D, 1D, 12D);
    public static final OptionDouble r = new OptionDouble("R",  6D, 1D, 12D);
    public static final OptionDouble g = new OptionDouble("G",  255D, 0D, 255D);
    public static final OptionDouble b = new OptionDouble("B",  0D, 0D, 255D);
    public static final OptionDouble a = new OptionDouble("A",  50D, 0D, 255D);


    public CityESPModule(){
        super("CityESP", null, "NONE", Color.CYAN, ModuleType.RENDER);


    }
}
