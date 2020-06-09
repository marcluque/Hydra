package de.datasecs.hydra.example.server.udp;

import com.marcluque.hydra.example.shared.udp.ExampleUDPPacket;
import de.datasecs.hydra.shared.protocol.impl.HydraProtocol;

/*
 * Created with love by DataSecs on 17.12.19
 */
public class UDPServerProtocol extends HydraProtocol {

    public UDPServerProtocol() {
        registerPacket(ExampleUDPPacket.class);

        // Register packet listener
        registerListener(new UDPServerPacketListener());
    }
}