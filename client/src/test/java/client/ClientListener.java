package client;

import de.datasec.hydra.shared.handler.HydraSession;
import de.datasec.hydra.shared.protocol.packets.HydraPacketListener;
import de.datasec.hydra.shared.protocol.packets.PacketHandler;
import shared.SimplePacket;

/**
 * Created by DataSec on 30.09.2017.
 */
public class ClientListener implements HydraPacketListener {

    @PacketHandler
    public void onSimplePacket(SimplePacket simplePacket, HydraSession session) {
        System.out.println("FROM SERVER: " + simplePacket.getString());
    }
}
