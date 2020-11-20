package us.np.moodlymod.event.custom.entitylivingbase;

import net.minecraft.potion.Potion;
import us.np.moodlymod.event.custom.CustomEvent;

public class PlayerIsPotionActiveEvent extends CustomEvent {
    public Potion potion;
    public PlayerIsPotionActiveEvent(Potion potion) { super(); this.potion = potion; }
}
