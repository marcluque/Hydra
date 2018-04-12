package de.datasecs.hydra.example.server.serialization;

import de.datasecs.hydra.example.shared.serialization.ExampleSerializationPacket;
import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.protocol.packets.listener.HydraPacketListener;
import de.datasecs.hydra.shared.protocol.packets.listener.PacketHandler;

/**
 * Created with love by DataSecs on 03.11.2017.
 */
public class ExampleSerializationServerPacketListener implements HydraPacketListener {

    public ExampleSerializationServerPacketListener() {
        // Do something
    }

    @PacketHandler
    public void onSampleSerializationPacket(ExampleSerializationPacket exampleSerializationPacket, Session session) {
        System.out.println("\n---PACKET-LISTENER OUTPUT---");

        System.out.printf("Received from client: %s%n", exampleSerializationPacket);

        session.close();
        System.out.println("\nSession closed!");
    }
}