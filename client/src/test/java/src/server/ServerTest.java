package src.server;

import de.datasec.hydra.server.HydraServer;

/**
 * Created by DataSec on 30.09.2017.
 */
public class ServerTest {

    public static void main(String[] args) {
        new HydraServer.Builder("localhost", 8080, new ServerProtocol())
                .workerThreads(2)
                .bossThreads(1)
                .build();
    }
}
