package server;

import de.datasec.hydra.shared.protocol.HydraProtocol;
import shared.GetPacket;
import shared.SimplePacket;

/**
 * Created by DataSec on 30.09.2017.
 */
public class ServerProtocol extends HydraProtocol {

    public ServerProtocol() {
        registerPacket(SimplePacket.class);
        registerPacket(GetPacket.class);
        registerListener(new ServerListener());
    }
}
