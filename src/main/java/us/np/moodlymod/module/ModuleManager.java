package us.np.moodlymod.module;

import us.np.moodlymod.module.modules.combat.AutoCrystalModule;
import us.np.moodlymod.module.modules.combat.AutoTotemModule;
import us.np.moodlymod.module.modules.exploit.AntiHungerModule;
import us.np.moodlymod.module.modules.misc.AntiAFKModule;
import us.np.moodlymod.module.modules.misc.AutoRacismModule;
import us.np.moodlymod.module.modules.misc.DiscordRPCModule;
import us.np.moodlymod.module.modules.movement.ElytraFlyModule;
import us.np.moodlymod.module.modules.movement.SprintModule;
import us.np.moodlymod.module.modules.render.FullbrightModule;
import us.np.moodlymod.module.modules.render.MobOwnerModule;
import us.np.moodlymod.module.modules.ui.ClickGuiModule;
import us.np.moodlymod.module.modules.ui.HUDModule;
import us.np.moodlymod.module.modules.world.DonkeyLoggerModule;

import java.util.ArrayList;

public class ModuleManager {
    private ArrayList<Module> modules;

    public ModuleManager() {
        this.modules = new ArrayList<Module>();
    }

    public void setModules() {
        /* Combat */
        modules.add(new AutoTotemModule());
        modules.add(new AutoCrystalModule());
        /* Exploit */
        modules.add(new AntiHungerModule());
        /* Movement */
        modules.add(new ElytraFlyModule());
        modules.add(new SprintModule());
        /* Render */
        /* modules.add(new XRay()); */
        modules.add(new FullbrightModule());
        modules.add(new MobOwnerModule());
        /* Misc */
        modules.add(new AntiAFKModule());
        modules.add(new AutoRacismModule());
        modules.add(new DiscordRPCModule());
        /* World */
        modules.add(new DonkeyLoggerModule());
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


    public void onModuleEnable(Module module) {

    }

    public void onModuleDisable(Module module) {

    }
}