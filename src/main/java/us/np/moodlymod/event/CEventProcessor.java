package us.np.moodlymod.event;

import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listenable;
import me.zero.alpine.fork.listener.Listener;
import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.event.custom.entityplayer.PlayerDisconnectEvent;

public class CEventProcessor implements Listenable {
    @EventHandler
    private Listener<PlayerDisconnectEvent> playerDisconnectEventListener = new Listener<>(event -> {
        MoodlyMod.saveAllModules();
        MoodlyMod.friendsUtil.save();
    });
}