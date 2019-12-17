package de.datasecs.hydra.example.client.udp;

import de.datasecs.hydra.shared.protocol.impl.HydraProtocol;

/*
 * Created with <3 by DataSecs on 17.12.19
 */
public class UdpClientProtocol extends HydraProtocol {

    public UdpClientProtocol() {
        registerListener(new UdpClientPacketListener());
    }
}