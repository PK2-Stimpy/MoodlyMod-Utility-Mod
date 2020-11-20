package us.np.moodlymod.event.custom.entityplayersp;

import net.minecraft.entity.MoverType;
import us.np.moodlymod.event.custom.CustomEvent;

public class PlayerMoveEvent extends CustomEvent {
    public MoverType type;
    public double x, y, z;
    public PlayerMoveEvent(MoverType type, double x, double y, double z) {
        super();
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}