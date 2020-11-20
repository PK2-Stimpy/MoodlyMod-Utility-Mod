package us.np.moodlymod.event.custom.networkmanager;

import net.minecraft.network.Packet;
import us.np.moodlymod.event.custom.CustomEvent;

public class NetworkPacketEvent extends CustomEvent {
    private Packet packet;

    public NetworkPacketEvent(Packet packet) {
        super();
        this.packet = packet;
    }

    public Packet getPacket() { return packet; }
}