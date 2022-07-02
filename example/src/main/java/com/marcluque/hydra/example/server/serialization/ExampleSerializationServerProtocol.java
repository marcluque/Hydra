package com.marcluque.hydra.example.server.serialization;

import com.marcluque.hydra.example.server.ExampleServerPacketListener;
import com.marcluque.hydra.example.shared.serialization.ExampleSerializationPacket;
import com.marcluque.hydra.shared.protocol.impl.HydraProtocol;

/**
 * Created with love by marcluque on 03.11.2017.
 */
public class ExampleSerializationServerProtocol extends HydraProtocol {

    public ExampleSerializationServerProtocol() {
        // Register your packets and listener. This is a very important step! Otherwise Hydra can't work with them!
        registerPacket(ExampleSerializationPacket.class);
        registerListener(new ExampleServerPacketListener());
    }
}