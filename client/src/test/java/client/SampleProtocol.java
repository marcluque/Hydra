package client;

import de.datasec.hydra.shared.protocol.HydraProtocol;

/**
 * Created by DataSec on 03.11.2017.
 */
public class SampleProtocol extends HydraProtocol {

    public SampleProtocol() {
        // Register your packets and listener. This is a very important step! Otherwise Hydra can't work with them!
        registerPacket(SamplePacket.class);
        registerListener(new SamplePacketListener());
    }
}
