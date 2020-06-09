package com.marcluque.hydra.server;

import com.marcluque.hydra.shared.ArrayPacket;
import com.marcluque.hydra.shared.FinishedPacket;
import com.marcluque.hydra.shared.TestPacket;
import com.marcluque.hydra.shared.protocol.impl.HydraProtocol;

/**
 * Created by marcluque on 28.03.2019.
 */
public class TestServerProtocol extends HydraProtocol {

    public TestServerProtocol() {
        registerPacket(TestPacket.class);
        registerPacket(ArrayPacket.class);
        registerPacket(FinishedPacket.class);
        registerListener(new TestServerPacketListener());
    }
}