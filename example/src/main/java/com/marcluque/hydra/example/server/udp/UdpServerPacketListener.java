package com.marcluque.hydra.example.server.udp;

import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.protocol.packets.StandardPacket;
import com.marcluque.hydra.shared.protocol.packets.listener.HydraPacketListener;
import com.marcluque.hydra.shared.protocol.packets.listener.PacketHandler;

/*
 * Created with <3 by marcluque on 17.12.19
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