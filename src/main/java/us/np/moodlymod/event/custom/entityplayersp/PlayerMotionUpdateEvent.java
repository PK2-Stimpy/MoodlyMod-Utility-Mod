package us.np.moodlymod.event.custom.entityplayersp;

import us.np.moodlymod.event.custom.CustomEvent;

public class PlayerMotionUpdateEvent extends CustomEvent {
    private Era era;
    public PlayerMotionUpdateEvent(Era era) { super(); this.era = era; }
    public Era getEra() { return era; }
}