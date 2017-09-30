package src.client;

import de.datasec.hydra.shared.handler.HydraSession;
import de.datasec.hydra.shared.protocol.packets.Packet;
import de.datasec.hydra.shared.protocol.packets.PacketListener;
import src.SimplePacket;

/**
 * Created by DataSec on 30.09.2017.
 */
public class ClientListener implements PacketListener {

    @Override
    public void onPacket(Packet packet, HydraSession session) {
        System.out.println("From SERVER: " + ((SimplePacket) packet).getObject());
    }
}
