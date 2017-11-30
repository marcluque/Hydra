package client;

import de.datasec.hydra.client.Client;
import de.datasec.hydra.client.HydraClient;
import de.datasec.hydra.shared.handler.Session;

import java.net.StandardSocketOptions;
import java.util.Arrays;

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


        // Checks if the client is connected to its remote host
        if (client.isConnected()) {
            // Returns the session that was created for the client and its remote host
            session = client.getSession();
            System.out.println("Client is online!");
            System.out.printf("Socket address: %s%n", session.getAddress());
        }

        /* Send a packet to the server via the session the client has saved */
        // Sends a String, that is converted to a Object and an array, the type of the array is defined in SamplePacket.class
        session.send(new SamplePacket("This is a message", new String[]{"This", "is", "a", "message"}));
        // Sends a list, that is converted to a Object and the array, like above
        session.send(new SamplePacket(Arrays.asList("This", "is", "a", "message", "2"), new String[]{"This", "is", "a", "message", "2"}));

        // Closes the connection and releases all occupied resources
        //client.close();
    }
}