package client;

import de.datasec.hydra.shared.protocol.HydraProtocol;
import shared.GetPacket;
import shared.SimplePacket;

/**
 * Created by DataSec on 30.09.2017.
 */
public class ClientProtocol extends HydraProtocol {

    public ClientProtocol() {
        registerPacket(SimplePacket.class);
        registerPacket(GetPacket.class);
        registerListener(new ClientListener());
    }
}
