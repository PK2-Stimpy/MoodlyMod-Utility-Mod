package us.np.moodlymod.mixin.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.Wrapper;
import us.np.moodlymod.module.modules.render.ChamsModule;

import java.awt.*;

@Mixin({RenderEnderCrystal.class})
public class MixinRenderEnderCrystal {
    @Shadow
    public ModelBase modelEnderCrystal;
    @Shadow
    public ModelBase modelEnderCrystalNoBase;
    @Final
    @Shadow
    private static ResourceLocation ENDER_CRYSTAL_TEXTURES;

    @Redirect(method = {"doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void render1(final ModelBase modelBase, final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        ChamsModule chams = (ChamsModule)MoodlyMod.moduleManager.getModuleByClass(ChamsModule.class);
        if (chams != null)
            if(chams.isEnabled() && ChamsModule.crystals.getValue() && ChamsModule.mode.getMode() == 0)
                return;
    }

    @Redirect(method = {"doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V", ordinal = 1))
    private void render2(final ModelBase modelBase, final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        ChamsModule chams = (ChamsModule)MoodlyMod.moduleManager.getModuleByClass(ChamsModule.class);
        if(chams != null)
            if(chams.isEnabled() && ChamsModule.crystals.getValue() && ChamsModule.mode.getMode() == 0)
                return;
    }

    @Inject(method = {"doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V"}, at = { @At("RETURN") }, cancellable = true)
    public void IdoRender(final EntityEnderCrystal entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo callback) {
        ChamsModule chams = (ChamsModule)MoodlyMod.moduleManager.getModuleByClass(ChamsModule.class);
        if (chams == null) return;

        if(chams.isEnabled() && ChamsModule.crystals.getValue() && ChamsModule.mode.getMode() == 6) { /* disabled esp */
            Color c = chams.rainbow.getValue() ? new Color(ChamsModule.Rainbow.rgb) : new Color(chams.r.getValue().intValue(), chams.g.getValue().intValue(), chams.b.getValue().intValue());
            GL11.glPushMatrix();
            final float f = entity.innerRotation + partialTicks;
            GlStateManager.translate(x, y, z);
            Wrapper.getMC().getRenderManager().renderEngine.bindTexture(MixinRenderEnderCrystal.ENDER_CRYSTAL_TEXTURES);
            float f2 = MathHelper.sin(f * 0.2f) / 2.0f + 0.5f;
            f2 += f2 * f2;
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1.0E7f);
            GL11.glPushAttrib(1048575);
            if (!chams.lines.getValue())
                GL11.glPolygonMode(1028, 6914);
            else
                GL11.glPolygonMode(1028, 6913);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glEnable(2848);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, chams.a.getValue().intValue() / 255.0f);
            if (chams.lines.getValue())
                GL11.glLineWidth(chams.width.getValue().floatValue());
            if (entity.shouldShowBottom())
                this.modelEnderCrystal.render((Entity)entity, 0.0f, f * 3.0f, f2 * 0.2f, 0.0f, 0.0f, 0.0625f);
            else
                this.modelEnderCrystalNoBase.render((Entity)entity, 0.0f, f * 3.0f, f2 * 0.2f, 0.0f, 0.0f, 0.0625f);
            GL11.glPopAttrib();
            GL11.glPolygonOffset(1.0f, 100000.0f);
            GL11.glDisable(32823);
            GL11.glPopMatrix();
        } else if (chams.isEnabled() && ChamsModule.crystals.getValue() && ChamsModule.mode.getMode() == 2) {
            GL11.glPushMatrix();
            final float f3 = entity.innerRotation + partialTicks;
            GlStateManager.translate(x, y, z);
            Wrapper.getMC().getRenderManager().renderEngine.bindTexture(MixinRenderEnderCrystal.ENDER_CRYSTAL_TEXTURES);
            float f4 = MathHelper.sin(f3 * 0.2f) / 2.0f + 0.5f;
            f4 += f4 * f4;
            GL11.glDisable(2929);
            GL11.glColor4f(chams.Wr.getValue().intValue() / 255.0f, chams.Wg.getValue().intValue() / 255.0f, chams.Wb.getValue().intValue() / 255.0f, 1.0f);
            GL11.glDisable(3553);
            if (entity.shouldShowBottom())
                this.modelEnderCrystal.render((Entity)entity, 0.0f, f3 * 3.0f, f4 * 0.2f, 0.0f, 0.0f, 0.0625f);
            else
                this.modelEnderCrystalNoBase.render((Entity)entity, 0.0f, f3 * 3.0f, f4 * 0.2f, 0.0f, 0.0f, 0.0625f);
            GL11.glEnable(2929);
            GL11.glColor4f(chams.Vr.getValue().intValue() / 255.0f, chams.Vg.getValue().intValue() / 255.0f, chams.Vb.getValue().intValue() / 255.0f, 1.0f);
            if (entity.shouldShowBottom())
                this.modelEnderCrystal.render((Entity)entity, 0.0f, f3 * 3.0f, f4 * 0.2f, 0.0f, 0.0f, 0.0625f);
            else
                this.modelEnderCrystalNoBase.render((Entity)entity, 0.0f, f3 * 3.0f, f4 * 0.2f, 0.0f, 0.0f, 0.0625f);
            GL11.glEnable(3553);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }
}