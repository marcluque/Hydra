package src.server;

import de.datasec.hydra.shared.handler.HydraSession;
import de.datasec.hydra.shared.protocol.packets.Packet;
import de.datasec.hydra.shared.protocol.packets.PacketListener;
import src.SimplePacket;

/**
 * Created by DataSec on 30.09.2017.
 */
public class ServerListener implements PacketListener {

    @Override
    public void onPacket(Packet packet, HydraSession session) {
        System.out.println("RECEIVED: " + ((SimplePacket) packet).getObject());
        session.send(new SimplePacket("WOOOOW"));
    }
}
