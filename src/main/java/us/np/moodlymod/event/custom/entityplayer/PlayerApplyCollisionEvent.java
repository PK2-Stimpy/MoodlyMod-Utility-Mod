package us.np.moodlymod.event.custom.entityplayer;

import net.minecraft.entity.Entity;
import us.np.moodlymod.event.custom.CustomEvent;

public class PlayerApplyCollisionEvent extends CustomEvent {
    public Entity entity;
    public PlayerApplyCollisionEvent(Entity entity) { super(); this.entity = entity; }
}
