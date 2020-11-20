package us.np.moodlymod.mixin;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import us.np.moodlymod.MoodlyMod;

import java.util.Map;

public class MixinLoaderForge implements IFMLLoadingPlugin {
    private static boolean isObfuscatedEnvironment = false;

    public MixinLoaderForge() {
        MoodlyMod.logger.info("MoodlyMod mixins init.");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.moodlymod.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        MoodlyMod.logger.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        isObfuscatedEnvironment = (boolean) (Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}