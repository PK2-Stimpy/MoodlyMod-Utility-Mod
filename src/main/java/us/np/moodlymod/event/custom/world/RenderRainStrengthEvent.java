package us.np.moodlymod.event.custom.world;

import us.np.moodlymod.event.custom.CustomEvent;

public class RenderRainStrengthEvent extends CustomEvent {
    public float delta;
    public RenderRainStrengthEvent(float delta) { super(); this.delta = delta; }
}