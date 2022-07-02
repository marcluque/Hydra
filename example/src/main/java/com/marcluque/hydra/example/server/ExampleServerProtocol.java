package com.marcluque.hydra.example.server;

import com.marcluque.hydra.example.shared.ExamplePacket;
import com.marcluque.hydra.example.shared.serialization.ExampleSerializationPacket;
import com.marcluque.hydra.shared.protocol.impl.HydraProtocol;

/**
 * Created with love by marcluque on 03.11.2017.
 */
public class ExampleServerProtocol extends HydraProtocol {

    public ExampleServerProtocol() {
        // Register your packets and listener. This is a very important step! Otherwise Hydra can't work with them!
        registerPacket(ExamplePacket.class);
        registerPacket(ExampleSerializationPacket.class);
        registerListener(new ExampleServerPacketListener());
    }
}