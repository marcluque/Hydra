package de.datasecs.hydra.example.client.udp;

import com.marcluque.hydra.example.shared.udp.ExampleUDPPacket;
import de.datasecs.hydra.shared.protocol.impl.HydraProtocol;

/*
 * Created with love by DataSecs on 17.12.19
 */
public class UDPClientProtocol extends HydraProtocol {

    public UDPClientProtocol() {
        registerPacket(ExampleUDPPacket.class);
        registerListener(new UDPClientPacketListener());
    }
}