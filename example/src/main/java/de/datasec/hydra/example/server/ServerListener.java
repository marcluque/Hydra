package de.datasec.hydra.example.server;

import de.datasec.hydra.example.SimplePacket;
import de.datasec.hydra.shared.handler.HydraSession;
import de.datasec.hydra.shared.protocol.packets.Packet;
import de.datasec.hydra.shared.protocol.packets.PacketListener;

/**
 * Created by DataSec on 30.09.2017.
 */
public class ServerListener implements PacketListener {

    @Override
    public void onPacket(Packet packet, HydraSession session) {
        System.out.println("RECEIVED: " + ((SimplePacket) packet).getString());
        session.send(new SimplePacket("WOOOOW"));
    }
}
