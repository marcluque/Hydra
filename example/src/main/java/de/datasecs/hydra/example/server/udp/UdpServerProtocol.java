package de.datasecs.hydra.example.server.udp;

import de.datasecs.hydra.shared.protocol.impl.HydraProtocol;

/*
 * Created with <3 by DataSecs on 17.12.19
 */
public class UdpServerProtocol extends HydraProtocol {

    public UdpServerProtocol() {
        // Register packet listener
        registerListener(new UdpServerPacketListener());
    }
}