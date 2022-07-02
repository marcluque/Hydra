package com.marcluque.hydra.example.server.udp;

import com.marcluque.hydra.example.shared.udp.ExampleUDPPacket;
import com.marcluque.hydra.shared.protocol.impl.HydraProtocol;

/*
 * Created with love by marcluque on 17.12.19
 */
public class UDPServerProtocol extends HydraProtocol {

    public UDPServerProtocol() {
        registerPacket(ExampleUDPPacket.class);

        // Register packet listener
        registerListener(new UDPServerPacketListener());
    }
}