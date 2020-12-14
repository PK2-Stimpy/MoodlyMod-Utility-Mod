package us.np.moodlymod.module;

import us.np.moodlymod.module.modules.combat.AutoCrystalModule;
import us.np.moodlymod.module.modules.combat.AutoTotemModule;
import us.np.moodlymod.module.modules.combat.VelocityModule;
import us.np.moodlymod.module.modules.combat.WorkingAutoTotemModule;
import us.np.moodlymod.module.modules.exploit.AntiHungerModule;
import us.np.moodlymod.module.modules.exploit.PacketCancellerModule;
import us.np.moodlymod.module.modules.misc.*;
import us.np.moodlymod.module.modules.movement.ElytraFlyModule;
import us.np.moodlymod.module.modules.movement.SprintModule;
import us.np.moodlymod.module.modules.render.*;
import us.np.moodlymod.module.modules.ui.ClickGuiModule;
import us.np.moodlymod.module.modules.ui.HUDModule;
import us.np.moodlymod.module.modules.world.EntityLoggerModule;

import java.util.ArrayList;

public class ModuleManager {
    private ArrayList<Module> modules;

    public ModuleManager() {
        this.modules = new ArrayList<Module>();
    }

    public void setModules() {
        /* Combat */
        modules.add(new WorkingAutoTotemModule());
        modules.add(new VelocityModule());
        /*
        modules.add(new AutoTotemModule());
        modules.add(new AutoCrystalModule());
        */
        
        /* Exploit */
        modules.add(new AntiHungerModule());
        modules.add(new PacketCancellerModule());
        /* Movement */
        modules.add(new ElytraFlyModule());
        modules.add(new SprintModule());
        /* Render */
        /* modules.add(new XRay()); */
        modules.add(new BreakESPModule());
        modules.add(new ChamsModule());
        modules.add(new FullbrightModule());
        modules.add(new MobOwnerModule());
        modules.add(new PortalESPModule());
        modules.add(new VoidESPModule());
        /* Misc */
        modules.add(new AnarchyColoredChatModule());
        modules.add(new AntiAFKModule());
        modules.add(new AutoRacismModule());
        modules.add(new AutoSalDupeModule());
        modules.add(new ChatModule());
        modules.add(new ChatSuffixModule());
        modules.add(new DiscordRPCModule());
        modules.add(new NoVoidModule());
        /* World */
        modules.add(new EntityLoggerModule());
        /* UI */
        // modules.add(new TestBetterModeModule()); /* Just for testing. */
        modules.add(new ClickGuiModule());
        modules.add(new HUDModule());
        /* Always Enabled */
    }
    public ArrayList<Module> getModules() {
        return modules;
    }
    public ArrayList<Module> getModulesByCategory(ModuleType moduleType) {
        ArrayList<Module> modules = new ArrayList<Module>();
        for(Module module : getModules())
            if(module.getModuleType() == moduleType)
                modules.add(module);
        return modules;
    }
    public Module getModuleByClass(Class<? extends Module> moduleClass) {
        for(Module module : getModules())
            if(module.getClass() == moduleClass)
                return module;
        return null;
    }


    public void onModuleEnable(Module module) {

    }

    public void onModuleDisable(Module module) {

    }
}