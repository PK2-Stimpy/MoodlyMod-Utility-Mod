package us.np.moodlymod.event.custom.render;

import us.np.moodlymod.event.custom.CustomEvent;

public class RenderEvent extends CustomEvent {
    private float partialTicks;
    public RenderEvent(float partialTicks) { super(); this.partialTicks = partialTicks; }
    public float getPartialTicks() { return partialTicks; }
}