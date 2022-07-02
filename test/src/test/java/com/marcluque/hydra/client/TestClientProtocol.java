package com.marcluque.hydra.client;

import com.marcluque.hydra.shared.TestPacket;
import com.marcluque.hydra.shared.protocol.impl.HydraProtocol;

/**
 * Created by marcluque on 07.02.2019.
 */
public class TestClientProtocol extends HydraProtocol {

    public TestClientProtocol() {
        registerPacket(TestPacket.class);
        registerListener(new TestClientPacketListener());
    }
}