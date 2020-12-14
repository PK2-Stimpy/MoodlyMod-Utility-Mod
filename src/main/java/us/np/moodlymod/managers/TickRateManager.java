package us.np.moodlymod.managers;

import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listenable;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.util.math.MathHelper;
import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.event.custom.networkmanager.NetworkPacketEvent;

public class TickRateManager implements Listenable {
    public static TickRateManager INSTANCE;

    private long prevTime;
    private float[] ticks = new float[20];
    private int currentTick;

    public TickRateManager() {
        INSTANCE = this;

        this.prevTime = -1;
        for (int i = 0, len = this.ticks.length; i < len; i++)
            this.ticks[i] = 0.0f;

        MoodlyMod.EVENT_BUS.subscribe(this);
    }

    public float getTickRate() {
        int tickCount = 0;
        float tickRate = 0.0f;

        for (int i = 0; i < this.ticks.length; i++) {
            final float tick = this.ticks[i];

            if (tick > 0.0f) {
                tickRate += tick;
                tickCount++;
            }
        }

        return MathHelper.clamp((tickRate / tickCount), 0.0f, 20.0f);
    }

    @EventHandler
    private Listener<NetworkPacketEvent> packetEventListener = new Listener<>(event -> {
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            if (this.prevTime != -1) {
                this.ticks[this.currentTick % this.ticks.length] = MathHelper.clamp((20.0f / ((float) (System.currentTimeMillis() - this.prevTime) / 1000.0f)), 0.0f, 20.0f);
                this.currentTick++;
            }
            this.prevTime = System.currentTimeMillis();
        }
    });
}