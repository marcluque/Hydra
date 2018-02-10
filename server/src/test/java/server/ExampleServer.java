package server;

import de.datasec.hydra.server.HydraServer;
import de.datasec.hydra.server.Server;
import de.datasec.hydra.shared.handler.Session;
import de.datasec.hydra.shared.handler.listener.HydraSessionListener;
import io.netty.channel.ChannelOption;

/**
 * Created with love by DataSec on 02.11.2017.
 */
public class ExampleServer {

    public static void main(String[] args) {
        /*
         * The session listener adds a listener to the server that is supposed to be called when
         * a session is created (in this case, when a client connects). For demonstration purposes
         * this is done via a direct instantiation (anonymous class). It is advised to do this in a separate class
         * for clearness, especially when there are other methods than just the two small from the
         * SessionListener interface.
         */

        // The builder returns a server which you can use for several things
        HydraServer server = new Server.Builder("localhost", 8888, new SampleProtocol())
                .bossThreads(2)
                .workerThreads(4)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .addListener(new HydraSessionListener() {
                    @Override
                    public void onConnected(Session session) {
                        System.out.println("\nClient connected!");
                    }

                    @Override
                    public void onDisconnected(Session session) {
                        System.out.println("\nClient disconnected!");
                    }
                })
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