package server;

import de.datasec.hydra.shared.handler.HydraSession;
import de.datasec.hydra.shared.protocol.packets.HydraPacketListener;
import de.datasec.hydra.shared.protocol.packets.PacketHandler;
import shared.GetPacket;
import shared.SimplePacket;

/**
 * Created by DataSec on 30.09.2017.
 */
public class ServerListener implements HydraPacketListener {

    @PacketHandler
    public void onSimplePacket(SimplePacket simplePacket, HydraSession session) {
        System.out.println("RECEIVED FROM CLIENT: " + simplePacket.getString());
        //session.send(new SimplePacket("ANSWER FROM SERVER"));
    }

    @PacketHandler
    public void onGetPacket(HydraSession session, GetPacket getPacket) {
        System.out.println("SOMETHING NICE");
    }
}
