package de.datasecs.hydra.example.client.udp;

import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.protocol.packets.StandardPacket;
import de.datasecs.hydra.shared.protocol.packets.listener.HydraPacketListener;
import de.datasecs.hydra.shared.protocol.packets.listener.PacketHandler;

/*
 * Created with <3 by DataSecs on 17.12.19
 */
public class UdpClientPacketListener implements HydraPacketListener {

    @PacketHandler
    public void onStandardPacket(StandardPacket standardPacket, Session session) {
        System.out.println("\n---PACKET-LISTENER OUTPUT---");

        System.out.printf("Received from server using the StandardPacket: %s%nSession: %s", standardPacket, session);

        //session.close();
        //System.out.println("\nSession closed!");
    }
}