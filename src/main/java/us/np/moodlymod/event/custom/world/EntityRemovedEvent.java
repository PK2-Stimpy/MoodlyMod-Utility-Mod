package us.np.moodlymod.event.custom.world;

import net.minecraft.entity.Entity;
import us.np.moodlymod.event.custom.CustomEvent;

public class EntityRemovedEvent extends CustomEvent {
    private Entity entity;
    public EntityRemovedEvent(Entity entity) { super(); this.entity = entity; }

    public Entity getEntity() { return entity; }
}