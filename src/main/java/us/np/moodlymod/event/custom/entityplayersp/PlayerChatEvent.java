package us.np.moodlymod.event.custom.entityplayersp;

import us.np.moodlymod.event.custom.CustomEvent;

public class PlayerChatEvent extends CustomEvent {
    public final String message;

    public PlayerChatEvent(String message) { this.message = message; }
}