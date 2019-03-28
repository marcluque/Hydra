package de.datasecs.hydra.server;

import de.datasecs.hydra.shared.TestPacket;
import de.datasecs.hydra.shared.protocol.impl.HydraProtocol;

/**
 * Created by DataSec on 28.03.2019.
 */
public class TestServerProtocol extends HydraProtocol {

    public TestServerProtocol() {
        registerPacket(TestPacket.class);
        registerListener(new TestServerPacketListener());
    }
}