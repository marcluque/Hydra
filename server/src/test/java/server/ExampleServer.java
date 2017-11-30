package server;

import de.datasec.hydra.server.HydraServer;
import de.datasec.hydra.server.Server;

import java.net.StandardSocketOptions;

/**
 * Created with love by DataSec on 02.11.2017.
 */
public class ExampleServer {

    public static void main(String[] args) {
        // The builder returns a server which you can use for several things
        HydraServer server = new Server.Builder("localhost", 8888, new SampleProtocol())
                .bossThreads(2)
                .workerThreads(4)
                .option(StandardSocketOptions.TCP_NODELAY, true)
                .option(StandardSocketOptions.SO_KEEPALIVE, true)
                .childOption(StandardSocketOptions.TCP_NODELAY, true)
                .childOption(StandardSocketOptions.SO_KEEPALIVE, true)
                .build();

        // Check if server is actively running
        if (server.isActive()) {
            System.out.println("Server is online!");
            // Returns the local address of the server that was set in the constructor
            System.out.printf("Socket address: %s%n", server.getLocalAdress());
        }

        // As soon as a channel with a client is initialized it is added to the set
        // If no clients are connected the set is empty
        System.out.println("Sessions: " + server.getSessions());

        // Closes the server and releases the occupied resources
        //server.close();
    }
}