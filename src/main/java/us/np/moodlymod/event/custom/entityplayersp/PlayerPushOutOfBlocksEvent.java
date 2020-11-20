package us.np.moodlymod.event.custom.entityplayersp;

import us.np.moodlymod.event.custom.CustomEvent;

public class PlayerPushOutOfBlocksEvent extends CustomEvent {
    public double x, y, z;
    public PlayerPushOutOfBlocksEvent(double x, double y, double z) {
        super();
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
