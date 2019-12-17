package de.datasecs.hydra.example.server.udp;

import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.protocol.packets.StandardPacket;
import de.datasecs.hydra.shared.protocol.packets.listener.HydraPacketListener;
import de.datasecs.hydra.shared.protocol.packets.listener.PacketHandler;

/*
 * Created with <3 by DataSecs on 17.12.19
 */
public class UdpServerPacketListener implements HydraPacketListener {

    public UdpServerPacketListener() {
        // Do something
    }

    @PacketHandler
    public void onStandardPacket(StandardPacket standardPacket, Session session) {
        System.out.println("\n---PACKET-LISTENER OUTPUT---");

        System.out.printf("Received from client using the StandardPacket: %s%nSession: %s", standardPacket, session);

        session.send("Received!");

        //session.close();
        //System.out.println("\nSession closed!");
    }
}