package us.np.moodlymod.event.custom;

import me.zero.alpine.fork.event.type.Cancellable;
import us.np.moodlymod.Wrapper;

public class CustomEvent extends Cancellable {
    private Era era = Era.PRE;
    private final float partialTicks;

    public CustomEvent() {
        partialTicks = Wrapper.getMC().getRenderPartialTicks();
    }
    public CustomEvent(Era era) {
        this();
        this.era = era;
    }

    public Era getEra() { return era; }
    public void setEra(Era era) { this.era = era; }
    public float getPartialTicks() { return partialTicks; }

    public enum Era
    {
        PRE,
        PERI,
        POST
    }
}