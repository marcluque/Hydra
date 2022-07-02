package com.marcluque.hydra.example.client.serialization;

import com.marcluque.hydra.example.shared.serialization.ExampleSerializationPacket;
import com.marcluque.hydra.shared.protocol.impl.HydraProtocol;

/**
 * Created with love by marcluque on 11.04.18
 */
public class ExampleSerializationClientProtocol extends HydraProtocol {

    public ExampleSerializationClientProtocol() {
        // Register your packets and listeners. This is a very important step! Otherwise, Hydra can't work with them!
        registerPacket(ExampleSerializationPacket.class);
        // No listeners are registered as they are not needed in this case
    }
}
