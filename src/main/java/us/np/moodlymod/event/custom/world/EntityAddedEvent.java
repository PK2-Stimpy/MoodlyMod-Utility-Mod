package us.np.moodlymod.event.custom.world;

import net.minecraft.entity.Entity;
import us.np.moodlymod.event.custom.CustomEvent;

public class EntityAddedEvent extends CustomEvent {
    private Entity entity;
    public EntityAddedEvent(Entity entity) { super(); this.entity = entity; }

    public Entity getEntity() { return entity; }
}