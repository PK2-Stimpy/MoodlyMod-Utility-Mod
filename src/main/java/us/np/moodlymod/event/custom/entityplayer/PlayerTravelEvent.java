package us.np.moodlymod.event.custom.entityplayer;

import us.np.moodlymod.event.custom.CustomEvent;

public class PlayerTravelEvent extends CustomEvent {
    public float strafe, vertical, forward;
    public PlayerTravelEvent(float strafe, float vertical, float forward) {
        super();
        this.strafe = strafe;
        this.vertical = vertical;
        this.forward = forward;
    }
}
