package us.np.moodlymod;

import com.maxwell.kmeth.gui.GUI;
import me.pk2.util.Auth;
import me.zero.alpine.fork.bus.EventBus;
import me.zero.alpine.fork.bus.EventManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.opengl.Display;
import us.np.moodlymod.command.CommandManager;
import us.np.moodlymod.event.CEventProcessor;
import us.np.moodlymod.event.EventProcessor;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleManager;
import us.np.moodlymod.util.FriendsUtil;
import us.np.moodlymod.util.RenderUtils;

@Mod(modid = MoodlyMod.MODID, name = MoodlyMod.NAME, version = MoodlyMod.VERSION)
public class MoodlyMod {
    public static final EventBus EVENT_BUS = new EventManager();

    public static final boolean DEBUG = true;
    public static final void debug(String p, String print) { if(MoodlyMod.DEBUG) System.out.println("[" + p + "] " + print); }

    public static final String MODID = "moodlymod";
    public static final String NAME = "MoodlyMod";
    public static final String VERSION = "0.3";

    public static ModuleManager moduleManager;
    public static CommandManager commandManager;
    public static FriendsUtil friendsUtil;

    public static Logger logger;
    public static GUI clickGui;

    public static MoodlyMod INSTANCE;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        /* Authenticate */
        Auth.auth();

        INSTANCE = this;

        Display.setTitle(NAME + " v" + VERSION);

        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        /* M12LK3M1KL31M2KL312KL3 ML */
        logger.info("Mod initlialised :" + NAME);

        moduleManager = new ModuleManager();
        moduleManager.setModules();

        commandManager = new CommandManager();
        friendsUtil = new FriendsUtil();

        clickGui = new GUI();

        EventProcessor eventProcessor = new EventProcessor();
        MinecraftForge.EVENT_BUS.register(eventProcessor);
        FMLCommonHandler.instance().bus().register(eventProcessor);

        EVENT_BUS.subscribe(new CEventProcessor());
    }

    @EventHandler
    public void stopping(FMLServerStoppingEvent event) {

    }

    public static void saveAllModules() {
        debug("PlayerLoggedOutEvent", "Saving all modules");
        for(Module module : moduleManager.getModules())
            module.save();
        debug("PlayerLoggedOutEvent", "All modules has been saved!");
    }
}