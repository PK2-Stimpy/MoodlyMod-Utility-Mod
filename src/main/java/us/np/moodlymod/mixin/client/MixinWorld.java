package us.np.moodlymod.mixin.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.event.custom.world.EntityAddedEvent;
import us.np.moodlymod.event.custom.world.EntityRemovedEvent;
import us.np.moodlymod.event.custom.world.RenderRainStrengthEvent;
import us.np.moodlymod.event.custom.world.WorldSetBlockStateEvent;

@Mixin(World.class)
public class MixinWorld {
    @Inject(method = "getRainStrength", at = @At("HEAD"), cancellable = true)
    public void onGetRainStrength(float delta, CallbackInfoReturnable<Float> callbackInfo) {
        RenderRainStrengthEvent event = new RenderRainStrengthEvent(delta);
        MoodlyMod.EVENT_BUS.post(event);
        if(event.isCancelled()) {
            callbackInfo.cancel();
            callbackInfo.setReturnValue(0.0f);
        }
    }

    @Inject(method = "setBlockState", at = @At("HEAD"), cancellable = true)
    public void onSetBlockState(BlockPos pos, IBlockState newState, int flags, CallbackInfoReturnable<Boolean> callbackInfo) {
        WorldSetBlockStateEvent event = new WorldSetBlockStateEvent(pos, newState, flags);
        MoodlyMod.EVENT_BUS.post(event);
        if(event.isCancelled()) {
            callbackInfo.cancel();
            callbackInfo.setReturnValue(false);
        }
    }

    @Inject(method = "onEntityAdded", at = @At("HEAD"), cancellable = true)
    public void onEntityAdded(Entity entity, CallbackInfo callbackInfo) {
        EntityAddedEvent entityAddedEvent = new EntityAddedEvent(entity);
        MoodlyMod.EVENT_BUS.post(entityAddedEvent);
        if(entityAddedEvent.isCancelled()) callbackInfo.cancel();
    }

    @Inject(method = "onEntityRemoved", at = @At("HEAD"), cancellable = true)
    public void onEntityRemoved(Entity entity, CallbackInfo callbackInfo) {
        EntityRemovedEvent entityAddedEvent = new EntityRemovedEvent(entity);
        MoodlyMod.EVENT_BUS.post(entityAddedEvent);
        if(entityAddedEvent.isCancelled()) callbackInfo.cancel();
    }
}