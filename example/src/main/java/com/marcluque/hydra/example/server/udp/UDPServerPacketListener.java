package com.marcluque.hydra.example.server.udp;

import com.marcluque.hydra.example.server.serialization.ExampleSerializationServer;
import com.marcluque.hydra.example.shared.udp.ExampleUDPPacket;
import com.marcluque.hydra.shared.handler.impl.UDPSession;
import com.marcluque.hydra.shared.protocol.packets.listener.HydraPacketListener;
import com.marcluque.hydra.shared.protocol.packets.listener.PacketHandler;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Created with love by marcluque on 17.12.19
 */
public class UDPServerPacketListener implements HydraPacketListener {

    private static final Logger LOGGER = LogManager.getLogger(UDPServerPacketListener.class.getName());

    public UDPServerPacketListener() {
        // Do something
    }

    @SuppressWarnings("unused") // Methods annotated with @PacketHandler are called at runtime by Hydra
    @PacketHandler
    public void onExampleUDPPacket(ExampleUDPPacket exampleUDPPacket, UDPSession session) {
        LOGGER.log(Level.INFO, "\n---PACKET-LISTENER OUTPUT---");
        LOGGER.log(Level.INFO, String.format("Received from client using the ExampleUDPPacket: %s%nSession: %s", exampleUDPPacket, session));

        session.send(new ExampleUDPPacket("Received!", session.getSender()));

        LOGGER.log(Level.INFO, String.format("Session: %s%n", session));
        LOGGER.log(Level.INFO, String.format("Session active: %s%n", session.isActive()));
        LOGGER.log(Level.INFO, String.format("Sender address: %s%n", session.getSender()));
        LOGGER.log(Level.INFO, String.format("Channel: %s%n%n", session.getChannel()));

        // session.close();
        // LOGGER.log(Level.INFO, "\nSession closed!");
    }
}