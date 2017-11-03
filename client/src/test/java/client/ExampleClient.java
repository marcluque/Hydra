package client;

import de.datasec.hydra.client.HydraClient;
import de.datasec.hydra.shared.handler.Session;

import java.net.StandardSocketOptions;

/**
 * Created by DataSec on 02.11.2017.
 */
public class ExampleClient {

    public static void main(String[] args) {
        // The builder returns a session which you can use for several things
        Session session = new HydraClient.Builder("localhost", 8888, new SampleProtocol())
                .workerThreads(4)
                .option(StandardSocketOptions.TCP_NODELAY, true)
                .option(StandardSocketOptions.SO_KEEPALIVE, true)
                .build();

        // Example for some things you can get from the session
        if (session.isConnected()) {
            System.out.println("Client is online!");
            System.out.printf("Socket address: %s%n", session.getRemoteAddress());
        }

        // Send a packet to the server via the session the builder returns
        session.send(new SamplePacket("Message for the server from the client"));
    }
}