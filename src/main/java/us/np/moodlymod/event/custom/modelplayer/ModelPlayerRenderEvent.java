package us.np.moodlymod.event.custom.modelplayer;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import us.np.moodlymod.event.custom.CustomEvent;

public class ModelPlayerRenderEvent extends CustomEvent {
    public ModelBase modelBase;
    public Entity entity;
    public float limbSwing;
    public float limbSwingAmount;
    public float ageInTicks;
    public float netHeadYaw;
    public float headPitch;
    public float scaleFactor;

    public ModelPlayerRenderEvent(final ModelBase modelBaseIn, final Entity entityIn, final float limbSwingIn, final float limbSwingAmountIn, final float ageInTicksIn, final float netHeadYawIn, final float headPitchIn, final float scaleFactorIn) {
        super();
        this.modelBase = modelBaseIn;
        this.entity = entityIn;
        this.limbSwing = limbSwingIn;
        this.limbSwingAmount = limbSwingAmountIn;
        this.ageInTicks = ageInTicksIn;
        this.netHeadYaw = netHeadYawIn;
        this.headPitch = headPitchIn;
        this.scaleFactor = scaleFactorIn;
    }
}