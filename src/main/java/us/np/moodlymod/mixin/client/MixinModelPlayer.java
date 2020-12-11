package us.np.moodlymod.mixin.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.event.custom.CustomEvent;
import us.np.moodlymod.event.custom.modelplayer.ModelPlayerRenderEvent;

@Mixin(value = {ModelPlayer.class}, priority = 9999)
public class MixinModelPlayer {
    @Shadow
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) { }

    @Inject(method = {"render"}, at = {@At("HEAD")}, cancellable = true)
    private void renderPre(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale, final CallbackInfo ci) {
        final ModelPlayerRenderEvent event = new ModelPlayerRenderEvent(ModelPlayer.class.cast(this), entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        event.setEra(CustomEvent.Era.PRE);
        MoodlyMod.EVENT_BUS.post(event);
        if(event.isCancelled())
            ci.cancel();
    }

    @Inject(method = {"render"}, at = {@At("RETURN")})
    private void renderPost(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale, final CallbackInfo ci) {
        final ModelPlayerRenderEvent event = new ModelPlayerRenderEvent(ModelPlayer.class.cast(this), entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        event.setEra(CustomEvent.Era.POST);
        MoodlyMod.EVENT_BUS.post(event);
    }
}