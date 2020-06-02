package de.datasecs.hydra.example.server.udp;

import de.datasecs.hydra.server.HydraServer;
import de.datasecs.hydra.server.Server;
import io.netty.channel.ChannelOption;

/*
 * Created with <3 by DataSecs on 17.12.19
 */
public class UdpServer {

    public static void main(String[] args) {
        HydraServer server = new Server.Builder("localhost", 8888, new UdpServerProtocol())
                .useUDP(true)
                .childOption(ChannelOption.SO_BROADCAST, true)
                .build();

        // Check if server is actively running (not obligatory)
        if (server.isActive()) {
            System.out.println("Server is online!");
            // Returns the local address of the server that was set in the constructor
            System.out.printf("Socket address: %s%n", server.getLocalAdress());
        }

        // As soon as a channel with a client is initialized it is added to the set of sessions
        // If no clients are connected the set is empty
        System.out.println("Sessions: " + server.getSessions());
    }
}