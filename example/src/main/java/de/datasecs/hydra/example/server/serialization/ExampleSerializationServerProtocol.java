package de.datasecs.hydra.example.server.serialization;

import de.datasecs.hydra.example.server.ExampleServerPacketListener;
import de.datasecs.hydra.example.shared.serialization.ExampleSerializationPacket;
import de.datasecs.hydra.shared.protocol.HydraProtocol;

/**
 * Created with love by DataSecs on 03.11.2017.
 */
public class ExampleSerializationServerProtocol extends HydraProtocol {

    public ExampleSerializationServerProtocol() {
        // Register your packets and listener. This is a very important step! Otherwise Hydra can't work with them!
        registerPacket(ExampleSerializationPacket.class);
        registerListener(new ExampleServerPacketListener());
    }
}