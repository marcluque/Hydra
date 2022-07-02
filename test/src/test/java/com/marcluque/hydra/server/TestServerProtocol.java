package com.marcluque.hydra.server;

import com.marcluque.hydra.shared.TestPacket;
import com.marcluque.hydra.shared.protocol.impl.HydraProtocol;

/**
 * Created by marcluque on 28.03.2019.
 */
public class TestServerProtocol extends HydraProtocol {

    public TestServerProtocol() {
        registerPacket(TestPacket.class);
        registerListener(new TestServerPacketListener());
    }
}