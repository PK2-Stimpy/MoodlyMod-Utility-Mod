package us.np.moodlymod.module.modules.render;
//Made By FaxHack 16/12/20
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.BetterMode;
import us.np.moodlymod.module.option.OptionBetterMode;
import us.np.moodlymod.module.option.OptionBoolean;
import us.np.moodlymod.module.option.OptionDouble;

import java.awt.*;

public class HoleESPModule extends Module {
    public static final OptionBetterMode mode = new OptionBetterMode("Mode", 0 , BetterMode.construct("Pretty", "Solid", "Outline"));
    public static final OptionDouble offset = new OptionDouble("Height", 0.2, 0.0, 1.0);
    public static final OptionDouble range = new OptionDouble("Range", 6D, 1D, 12D);
    public static final OptionBoolean hide_own = new OptionBoolean("Hide Own", true);
    public static final OptionBoolean bedrock_enable = new OptionBoolean("Bedrock Holes", true);
    public static final OptionDouble rb = new OptionDouble("R", 0D, 0D, 0D);
    public static final OptionDouble gb = new OptionDouble("G", 255D, 0D, 0D);
    public static final OptionDouble bb = new OptionDouble("B", 0D, 0D, 255D);
    public static final OptionDouble ab = new OptionDouble("A", 50D, 0D, 255D   );
    public static final OptionBoolean obsidian_enable	= new OptionBoolean("Obsidian Holes",true);
    public static final OptionDouble ro = new OptionDouble("R", 255D, 0D,255D);
    public static final OptionDouble go = new OptionDouble("G", 0D, 0D, 255D);
    public static final OptionDouble bo = new OptionDouble("G",0D, 0D, 255D);
    public static final OptionDouble ao= new OptionDouble("B",50D, 0D, 255D);
    public static final OptionDouble line_a = new OptionDouble("Outline A", 255D, 0D,   255D);


    public HoleESPModule() {
        super("NameTags", null, "NONE", Color.CYAN, ModuleType.RENDER);
    }
}
