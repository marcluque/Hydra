package com.marcluque.hydra.example.client;

import com.marcluque.hydra.example.shared.ExamplePacket;
import com.marcluque.hydra.example.shared.serialization.ExampleSerializationPacket;
import com.marcluque.hydra.shared.protocol.impl.HydraProtocol;

/**
 * Created with love by marcluque on 11.04.18
 */
public class ExampleClientProtocol extends HydraProtocol {

    public ExampleClientProtocol() {
        // Register your packets and listener. This is a very important step! Otherwise Hydra can't work with them!
        registerPacket(ExamplePacket.class);
        registerPacket(ExampleSerializationPacket.class);
        registerListener(new ExampleClientPacketListener());
    }
}
