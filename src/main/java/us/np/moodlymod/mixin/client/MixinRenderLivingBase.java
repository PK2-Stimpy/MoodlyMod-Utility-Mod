package us.np.moodlymod.mixin.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.event.custom.CustomEvent;
import us.np.moodlymod.event.custom.renderlivingbase.ModelRenderEvent;
import us.np.moodlymod.event.custom.renderlivingbase.PostRenderLayersEvent;

@Mixin(value = { RenderLivingBase.class }, priority = Integer.MAX_VALUE)
public abstract class MixinRenderLivingBase {
    @Shadow
    protected ModelBase mainModel;

    @Redirect(method = { "renderModel" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void renderModelWrapper(final ModelBase modelBase, final Entity entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor) {
        ModelRenderEvent preEvent = new ModelRenderEvent(modelBase, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        preEvent.setEra(CustomEvent.Era.PRE);
        MoodlyMod.EVENT_BUS.post(preEvent);
        if(preEvent.isCancelled())
            return;

        modelBase.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        ModelRenderEvent postEvent = new ModelRenderEvent(modelBase, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        postEvent.setEra(CustomEvent.Era.POST);
        MoodlyMod.EVENT_BUS.post(postEvent);
    }

    @Inject(method = { "renderLayers" }, at = { @At("RETURN") })
    public void renderLayers(final EntityLivingBase entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleIn, final CallbackInfo ci) {
        PostRenderLayersEvent event = new PostRenderLayersEvent(RenderLivingBase.class.cast(this), this.mainModel, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scaleIn);
        event.setEra(CustomEvent.Era.POST);
        MoodlyMod.EVENT_BUS.post(event);
    }
}