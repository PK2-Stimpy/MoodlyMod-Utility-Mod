package us.np.moodlymod.mixin.client;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.event.custom.CustomEvent;
import us.np.moodlymod.event.custom.entityplayersp.*;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP {
    @Inject(method = "onUpdate", at = @At("HEAD"), cancellable = true)
    public void onUpdate(CallbackInfo callbackInfo) {
        PlayerUpdateEvent playerUpdateEvent = new PlayerUpdateEvent();
        MoodlyMod.EVENT_BUS.post(playerUpdateEvent);
        if(playerUpdateEvent.isCancelled()) callbackInfo.cancel();
    }

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void move(MoverType type, double x, double y, double z, CallbackInfo callbackInfo)
    {
        PlayerMoveEvent playerMoveEvent = new PlayerMoveEvent(type, x, y, z);
        MoodlyMod.EVENT_BUS.post(playerMoveEvent);
        if(playerMoveEvent.isCancelled()) callbackInfo.cancel();
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
    public void onPreUpdateWalkingPlayer(CallbackInfo callbackInfo)
    {
        PlayerMotionUpdateEvent playerMotionUpdateEvent = new PlayerMotionUpdateEvent(CustomEvent.Era.PRE);
        MoodlyMod.EVENT_BUS.post(playerMotionUpdateEvent);
        if(playerMotionUpdateEvent.isCancelled()) callbackInfo.cancel();
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("RETURN"), cancellable = true)
    public void onPostUpdateWalkingPlayer(CallbackInfo callbackInfo)
    {
        PlayerMotionUpdateEvent playerMotionUpdateEvent = new PlayerMotionUpdateEvent(CustomEvent.Era.POST);
        MoodlyMod.EVENT_BUS.post(playerMotionUpdateEvent);
        if(playerMotionUpdateEvent.isCancelled()) callbackInfo.cancel();
    }

    @Inject(method = "swingArm", at = @At("HEAD"), cancellable = true)
    public void onSwingArm(EnumHand hand, CallbackInfo callbackInfo)
    {
        PlayerSwingArmEvent playerSwingArmEvent = new PlayerSwingArmEvent(hand);
        MoodlyMod.EVENT_BUS.post(playerSwingArmEvent);
        if(playerSwingArmEvent.isCancelled()) callbackInfo.cancel();
    }

    @Inject(method = "pushOutOfBlocks(DDD)Z", at = @At("HEAD"), cancellable = true)
    public void onPushOutOfBlocks(double x, double y, double z, CallbackInfoReturnable<Boolean> callbackInfo)
    {
        PlayerPushOutOfBlocksEvent playerPushOutOfBlocksEvent = new PlayerPushOutOfBlocksEvent(x, y, z);
        MoodlyMod.EVENT_BUS.post(playerPushOutOfBlocksEvent);
        if(playerPushOutOfBlocksEvent.isCancelled()) callbackInfo.setReturnValue(false);
    }
}