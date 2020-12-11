package us.np.moodlymod.mixin.client;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({Render.class})
public abstract class MixinRenderer<T extends Entity> {
    @Shadow
    protected abstract boolean bindEntityTexture(final T p0);
}