package src.client;

import de.datasec.hydra.shared.protocol.HydraProtocol;
import src.SimplePacket;

/**
 * Created by DataSec on 30.09.2017.
 */
public class ClientProtocol extends HydraProtocol {

    public ClientProtocol() {
        registerPacket(SimplePacket.class);
        registerListener(new ClientListener());
    }
}
