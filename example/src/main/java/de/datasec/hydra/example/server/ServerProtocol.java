package de.datasec.hydra.example.server;

import de.datasec.hydra.example.SimplePacket;
import de.datasec.hydra.shared.protocol.HydraProtocol;

/**
 * Created by DataSec on 30.09.2017.
 */
public class ServerProtocol extends HydraProtocol {

    public ServerProtocol() {
        registerPacket(SimplePacket.class);
        registerListener(new ServerListener());
    }
}
