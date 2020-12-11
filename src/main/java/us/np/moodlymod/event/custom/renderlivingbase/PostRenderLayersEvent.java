package us.np.moodlymod.event.custom.renderlivingbase;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import us.np.moodlymod.event.custom.CustomEvent;

public class PostRenderLayersEvent extends CustomEvent {
    public RenderLivingBase renderer;
    public ModelBase modelBase;
    public EntityLivingBase entity;
    public float limbSwing;
    public float limbSwingAmount;
    public float partialTicks;
    public float ageInTicks;
    public float netHeadYaw;
    public float headPitch;
    public float scaleIn;

    public PostRenderLayersEvent(final RenderLivingBase renderer, final ModelBase modelBase, final EntityLivingBase entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleIn) {
        super();
        this.renderer = renderer;
        this.modelBase = modelBase;
        this.entity = entitylivingbaseIn;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.partialTicks = partialTicks;
        this.ageInTicks = ageInTicks;
        this.netHeadYaw = netHeadYaw;
        this.headPitch = headPitch;
        this.scaleIn = scaleIn;
    }
}