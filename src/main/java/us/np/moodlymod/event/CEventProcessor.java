package us.np.moodlymod.event;

import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listenable;
import me.zero.alpine.fork.listener.Listener;
import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.event.custom.entityplayer.PlayerDisconnectEvent;

public class CEventProcessor implements Listenable {
    private Thread saveThread = null;

    @EventHandler
    private Listener<PlayerDisconnectEvent> playerDisconnectEventListener = new Listener<>(event -> {
        saveThread = new Thread(() -> {
            MoodlyMod.saveAllModules();
            MoodlyMod.friendsUtil.save();
        });
        saveThread.start();
    });
}