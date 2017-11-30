package client;

import de.datasec.hydra.client.Client;
import de.datasec.hydra.client.HydraClient;
import de.datasec.hydra.shared.handler.Session;

import java.net.StandardSocketOptions;

/**
 * Created with love by DataSec on 02.11.2017.
 */
public class ExampleClient {

    private static Session session;

    public static void main(String[] args) {
        // The builder returns a session which you can use for several things
        HydraClient client = new Client.Builder("localhost", 8888, new SampleProtocol())
                .workerThreads(4)
                .option(StandardSocketOptions.TCP_NODELAY, true)
                .option(StandardSocketOptions.SO_KEEPALIVE, true)
                .build();


        // Example for some things you can get from the session
        if (client.isConnected()) {
            session = client.getSession();
            System.out.println("Client is online!");
            System.out.printf("Socket address: %s%n", session.getAddress());
        }

        // Send a packet to the server via the session the client has saved
        session.send(new SamplePacket("Message for the server from the client"));

        // Closes the connection and releases all occupied resources
        //client.close();
    }
}