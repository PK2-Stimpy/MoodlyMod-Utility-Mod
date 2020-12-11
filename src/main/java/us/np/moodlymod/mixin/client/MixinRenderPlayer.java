package us.np.moodlymod.mixin.client;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.module.modules.render.ChamsModule;

import java.awt.*;

@Mixin({RenderPlayer.class})
public class MixinRenderPlayer {
    /*
    @Redirect(method = {"renderRightArm"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelRenderer;render(F)V"))
    private void renderRightArm(final ModelRenderer modelRenderer, final float scale) {
        ChamsModule module = (ChamsModule)MoodlyMod.moduleManager.getModuleByClass(ChamsModule.class);
        Color c = ChamsModule.rainbow.getValue() ? new Color(ChamsModule.Rainbow.rgb) : new Color(ChamsModule.r.getValue().intValue(), ChamsModule.g.getValue().intValue(), ChamsModule.b.getValue().intValue());
        if (module.hand.getValue() && module.isEnabled() && module.mode.getMode() == 0) {
            GL11.glPushMatrix();
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1.0E7f);
            GL11.glPushAttrib(1048575);
            if (!module.lines.getValue())
                GL11.glPolygonMode(1028, 6914);
            else
                GL11.glPolygonMode(1028, 6913);

            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glEnable(2848);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, module.a.getValue().intValue() / 255.0f);
            if (module.lines.getValue())
                GL11.glLineWidth(module.width.getValue().floatValue());
        }
    }
    */
}