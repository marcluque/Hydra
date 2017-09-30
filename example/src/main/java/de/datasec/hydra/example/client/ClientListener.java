package de.datasec.hydra.example.client;

import de.datasec.hydra.example.SimplePacket;
import de.datasec.hydra.shared.handler.HydraSession;
import de.datasec.hydra.shared.protocol.packets.Packet;
import de.datasec.hydra.shared.protocol.packets.PacketListener;

/**
 * Created by DataSec on 30.09.2017.
 */
public class ClientListener implements PacketListener {

    @Override
    public void onPacket(Packet packet, HydraSession session) {
        byte id = session.getProtocol().getPacketId(packet);

        switch (id) {
            case 1:
                onSimplePacket((SimplePacket) packet);
                break;
            default:
                throw new IllegalArgumentException(String.format("Packet with id %s not registered.", id));
        }
    }

    private void onSimplePacket(SimplePacket simplePacket) {
        System.out.println("From SERVER: " + simplePacket.getString());
    }
}
