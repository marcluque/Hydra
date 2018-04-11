package de.datasecs.hydra.example.shared;

import de.datasecs.hydra.example.server.SampleServerPacketListener;
import de.datasecs.hydra.example.shared.packets.SamplePacket;
import de.datasecs.hydra.example.shared.packets.SampleSerializationPacket;
import de.datasecs.hydra.shared.protocol.HydraProtocol;

/**
 * Created with love by DataSecs on 03.11.2017.
 */
public class SampleProtocol extends HydraProtocol {

    public SampleProtocol() {
        // Register your packets and listener. This is a very important step! Otherwise Hydra can't work with them!
        registerPacket(SamplePacket.class);
        registerPacket(SampleSerializationPacket.class);
        registerListener(new SampleServerPacketListener());
    }
}