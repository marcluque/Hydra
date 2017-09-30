package src.server;

import de.datasec.hydra.shared.protocol.HydraProtocol;
import src.SimplePacket;

/**
 * Created by DataSec on 30.09.2017.
 */
public class ServerProtocol extends HydraProtocol {

    public ServerProtocol() {
        registerPacket(SimplePacket.class);
        registerListener(new ServerListener());
    }
}
