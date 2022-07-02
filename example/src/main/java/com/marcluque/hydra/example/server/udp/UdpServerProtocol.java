package com.marcluque.hydra.example.server.udp;

import com.marcluque.hydra.shared.protocol.impl.HydraProtocol;

/*
 * Created with <3 by marcluque on 17.12.19
 */
public class UdpServerProtocol extends HydraProtocol {

    public UdpServerProtocol() {
        // Register packet listener
        registerListener(new UdpServerPacketListener());
    }
}