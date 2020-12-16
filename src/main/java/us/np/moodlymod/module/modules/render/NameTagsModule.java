package us.np.moodlymod.module.modules.render;
//Made By FaxHack 16/12/20
import me.zero.alpine.fork.listener.EventHandler;
import scala.None;
import scala.xml.Null;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.OptionBetterMode;
import us.np.moodlymod.module.option.OptionBoolean;
import us.np.moodlymod.module.option.OptionDouble;

import javax.xml.bind.Marshaller;
import java.awt.*;

public class NameTagsModule extends Module {
    public static final OptionBoolean show_armor = new OptionBoolean("Armor", true);
    public static final OptionBoolean show_health = new OptionBoolean("Health", true);
    public static final OptionBoolean show_totem  = new OptionBoolean("Totem", true);
    public static final OptionBoolean show_invis  = new OptionBoolean("Invis", true);
    public static final OptionBoolean show_reverse = new OptionBoolean("Armor Reverse", true);
    public static final OptionBoolean show_ping  = new OptionBoolean("Ping", false);
    public static final OptionDouble m_scale = new OptionDouble("Scale", 4D, 1D, 15D);
    public static final OptionDouble range = new OptionDouble("Range", 75D, 1D, 250D);
    public static final OptionDouble r = new OptionDouble("R",255D, 0D, 255D);
    public static final OptionDouble g = new OptionDouble("G",255D, 0D, 255D);
    public static final OptionDouble b = new OptionDouble("B",255D, 0D, 255D);
    public static final OptionDouble a = new OptionDouble("A",255D, 0D, 255D);
    public static final OptionBoolean rainbow_mode = new OptionBoolean("Rainbow", false);
    public static final OptionDouble sat = new OptionDouble("Saturation", 0.8D, 0D, 1D);
    public static final OptionDouble brightness = new OptionDouble("Brightness", 0.8D, 0D, 1D);


    public NameTagsModule(){
        super("NameTags", null, "NONE", Color.CYAN, ModuleType.RENDER);
        addOption(show_armor);
        addOption(show_health);
        addOption(show_totem);
        addOption(show_invis);
        addOption(show_reverse);
        addOption(show_ping);
        addOption(m_scale);
        addOption(range);
        addOption(range);
        addOption(r);
        addOption(g);
        addOption(b);
        addOption(a);
        addOption(rainbow_mode);
        addOption(sat);
        addOption(brightness);
        endOption();
    }
}
