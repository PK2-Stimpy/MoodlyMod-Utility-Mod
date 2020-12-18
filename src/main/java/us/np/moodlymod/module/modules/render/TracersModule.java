package us.np.moodlymod.module.modules.render;
//Made By FaxHack 16/12/20
import javassist.bytecode.stackmap.Tracer;
import scala.xml.Null;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.OptionBoolean;
import us.np.moodlymod.module.option.OptionDouble;

import java.awt.*;

public class TracersModule extends Module {
    public static final OptionBoolean friends = new OptionBoolean("Friends", true);
    public static final OptionDouble range = new OptionDouble("Range", 50D, 0D, 250D);
    public static final OptionDouble width  = new OptionDouble("Width", 1.0D, 0.0D, 5.0D);
    public static final OptionDouble offset = new OptionDouble("Offset", 0D, -4.0D, 4.0D);


    public TracersModule() {
        super("NameTags", null, "NONE", Color.CYAN, ModuleType.RENDER);
        addOption(friends);
        addOption(range);
        addOption(width);
        addOption(offset);
        endOption();
    }
}
