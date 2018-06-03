package de.datasecs.hydra.example.server;

import de.datasecs.hydra.example.shared.ExamplePacket;
import de.datasecs.hydra.example.shared.serialization.ExampleSerializationPacket;
import de.datasecs.hydra.shared.protocol.impl.HydraProtocol;

/**
 * Created with love by DataSecs on 03.11.2017.
 */
public class ExampleServerProtocol extends HydraProtocol {

    public ExampleServerProtocol() {
        // Register your packets and listener. This is a very important step! Otherwise Hydra can't work with them!
        registerPacket(ExamplePacket.class);
        registerPacket(ExampleSerializationPacket.class);
        registerListener(new ExampleServerPacketListener());
    }
}