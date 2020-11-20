package us.np.moodlymod.module.modules.ui;

import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.Wrapper;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;

import java.awt.*;

public class ClickGuiModule extends Module {
    public ClickGuiModule() {
        super("ClickGui", new String[] {""}, "P", Color.YELLOW, ModuleType.UI);
        endOption();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if(Wrapper.getMC().player != null)
            if(Wrapper.getMC().currentScreen == null) {
                Wrapper.getMC().displayGuiScreen(MoodlyMod.clickGui);
                if(this.isEnabled()) toggle(true);
            }
    }
}