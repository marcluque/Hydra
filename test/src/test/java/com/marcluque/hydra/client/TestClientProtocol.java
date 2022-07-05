package com.marcluque.hydra.client;

import com.marcluque.hydra.shared.ArrayPacket;
import com.marcluque.hydra.shared.FinishedPacket;
import com.marcluque.hydra.shared.TestPacket;
import com.marcluque.hydra.shared.protocol.impl.HydraProtocol;
import com.marcluque.hydra.shared.serialization.SerializationPacket;

/**
 * Created by marcluque on 07.02.2019.
 */
public class TestClientProtocol extends HydraProtocol {

    public TestClientProtocol() {
        registerPacket(TestPacket.class);
        registerPacket(ArrayPacket.class);
        registerPacket(SerializationPacket.class);
        registerPacket(FinishedPacket.class);
        registerListener(new TestClientPacketListener());
    }
}