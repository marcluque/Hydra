package client;

import de.datasec.hydra.client.HydraClient;
import de.datasec.hydra.shared.handler.HydraSession;
import shared.GetPacket;

import java.net.StandardSocketOptions;

/**
 * Created by DataSec on 30.09.2017.
 */
public class ClientTest {

    public static void main(String[] args) {
        HydraSession session = new HydraClient.Builder("localhost", 8080, new ClientProtocol())
                .workerThreads(2)
                .option(StandardSocketOptions.TCP_NODELAY, true)
                .build();

        session.send(new GetPacket("asdf"));
    }
}
