package com.marcluque.hydra.example.server.serialization;

import com.marcluque.hydra.example.shared.serialization.ExampleSerializationPacket;
import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.protocol.packets.listener.HydraPacketListener;
import com.marcluque.hydra.shared.protocol.packets.listener.PacketHandler;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created with love by marcluque on 03.11.2017.
 */
public class ExampleSerializationServerPacketListener implements HydraPacketListener {

    private static final Logger LOGGER = LogManager.getLogger(ExampleSerializationServerPacketListener.class.getName());

    public ExampleSerializationServerPacketListener() {
        // Do something
    }

    @SuppressWarnings("unused") // Methods annotated with @PacketHandler are called at runtime by Hydra
    @PacketHandler
    public void onSampleSerializationPacket(ExampleSerializationPacket exampleSerializationPacket, Session session) {
        LOGGER.log(Level.INFO, "%n---PACKET-LISTENER OUTPUT---%n");

        LOGGER.log(Level.INFO, "Received from client: {}%n", exampleSerializationPacket);

        session.close();
        LOGGER.log(Level.INFO, "%nSession closed!%n");
    }
}