package us.np.moodlymod.mixin.client;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.event.custom.entityplayer.PlayerApplyCollisionEvent;
import us.np.moodlymod.event.custom.entityplayer.PlayerPushedByWaterEvent;
import us.np.moodlymod.event.custom.entityplayer.PlayerTravelEvent;

@Mixin(value = EntityPlayer.class, priority = Integer.MAX_VALUE)
public abstract class MixinEntityPlayer extends MixinEntityLivingBase {
    public MixinEntityPlayer() { super(); }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    public void travel(float strafe, float vertical, float forward, CallbackInfo callbackInfo) {
        PlayerTravelEvent event = new PlayerTravelEvent(strafe, vertical, forward);
        MoodlyMod.EVENT_BUS.post(event);
        if(event.isCancelled()) {
            move(MoverType.SELF, motionX, motionY, motionZ);
            callbackInfo.cancel();
        }
    }

    @Inject(method = "applyEntityCollision", at = @At("HEAD"), cancellable = true)
    public void applyEntityCollision(Entity entity, CallbackInfo callbackInfo) {
        PlayerApplyCollisionEvent event = new PlayerApplyCollisionEvent(entity);
        MoodlyMod.EVENT_BUS.post(event);
        if(event.isCancelled()) callbackInfo.cancel();
    }

    @Inject(method = "isPushedByWater()Z", at = @At("HEAD"), cancellable = true)
    public void isPushedByWater(CallbackInfoReturnable<Boolean> callbackInfo)
    {
        PlayerPushedByWaterEvent event = new PlayerPushedByWaterEvent();
        MoodlyMod.EVENT_BUS.post(event);
        if(event.isCancelled()) callbackInfo.setReturnValue(false);
    }
}