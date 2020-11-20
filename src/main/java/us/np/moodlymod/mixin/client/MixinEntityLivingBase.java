package us.np.moodlymod.mixin.client;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.event.custom.entitylivingbase.PlayerIsPotionActiveEvent;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends MixinEntity {
    public MixinEntityLivingBase() { super(); }

    @Shadow public void jump() { }
    @Inject(method = "isPotionActive", at = @At("HEAD"), cancellable = true)
    public void isPotionActive(Potion potion, CallbackInfoReturnable<Boolean> callbackInfo) {
        PlayerIsPotionActiveEvent event = new PlayerIsPotionActiveEvent(potion);
        MoodlyMod.EVENT_BUS.post(event);
        if(event.isCancelled()) callbackInfo.setReturnValue(false);
    }
}
