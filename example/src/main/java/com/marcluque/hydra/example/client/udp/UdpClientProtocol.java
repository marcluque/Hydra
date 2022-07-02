package com.marcluque.hydra.example.client.udp;

import com.marcluque.hydra.shared.protocol.impl.HydraProtocol;

/*
 * Created with <3 by marcluque on 17.12.19
 */
public class UdpClientProtocol extends HydraProtocol {

    public UdpClientProtocol() {
        registerListener(new UdpClientPacketListener());
    }
}