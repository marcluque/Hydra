package de.datasecs.hydra.shared;

import de.datasecs.hydra.shared.protocol.impl.HydraProtocol;

/**
 * Created by DataSec on 07.02.2019.
 */
public class TestProtocol extends HydraProtocol {

    public TestProtocol() {
        registerPacket(TestPacket.class);
    }
}
