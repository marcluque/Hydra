package com.marcluque.hydra.example.server.serialization;

import com.marcluque.hydra.example.shared.serialization.ExampleSerializationPacket;
import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.protocol.packets.listener.HydraPacketListener;
import com.marcluque.hydra.shared.protocol.packets.listener.PacketHandler;

/**
 * Created with love by marcluque on 03.11.2017.
 */
public class ExampleSerializationServerPacketListener implements HydraPacketListener {

    public ExampleSerializationServerPacketListener() {
        // Do something
    }

    @SuppressWarnings("unused") // Methods annotated with @PacketHandler are called at runtime by Hydra
    @PacketHandler
    public void onSampleSerializationPacket(ExampleSerializationPacket exampleSerializationPacket, Session session) {
        System.out.println("\n---PACKET-LISTENER OUTPUT---");

        System.out.printf("Received from client: %s%n", exampleSerializationPacket);

        session.close();
        System.out.println("\nSession closed!");
    }
}