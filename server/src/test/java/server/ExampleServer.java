package server;

import de.datasec.hydra.server.HydraServer;
import de.datasec.hydra.shared.handler.Session;

import java.net.StandardSocketOptions;

/**
 * Created by DataSec on 02.11.2017.
 */
public class ExampleServer {

    public static void main(String[] args) {
        // The builder returns a session which you can use for several things
        Session session = new HydraServer.Builder("localhost", 8888, new SampleProtocol())
                .bossThreads(2)
                .workerThreads(4)
                .option(StandardSocketOptions.TCP_NODELAY, true)
                .option(StandardSocketOptions.SO_KEEPALIVE, true)
                .childOption(StandardSocketOptions.TCP_NODELAY, true)
                .childOption(StandardSocketOptions.SO_KEEPALIVE, true)
                .build();

        // Example for some things you can get from the session
        if (session.isConnected()) {
            System.out.println("Server is online!");
            System.out.printf("Socket address: %s%n", session.getRemoteAddress());
        }
    }
}