package us.np.moodlymod.event.custom.entityplayersp;

import net.minecraft.util.EnumHand;
import us.np.moodlymod.event.custom.CustomEvent;

public class PlayerSwingArmEvent extends CustomEvent {
    public EnumHand hand;
    public PlayerSwingArmEvent(EnumHand hand) { super(); this.hand = hand; }
}