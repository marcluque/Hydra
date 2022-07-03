package com.marcluque.hydra.example.server;

import com.marcluque.hydra.server.HydraServer;
import com.marcluque.hydra.server.Server;
import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.handler.listener.HydraSessionListener;
import io.netty.channel.ChannelOption;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created with love by marcluque on 02.11.2017.
 */
public class ExampleServer {

    private static final Logger LOGGER = LogManager.getLogger(ExampleServer.class.getName());

    public static void main(String[] args) {
        /*
         * The session listener adds a listener to the server that is supposed to be called when
         * a session is created (in this case, when a client connects). For demonstration purposes
         * this is done via a direct instantiation (anonymous class). It is advised to do this in a separate class
         * for clearness, especially when there are other methods than just the two small from the
         * SessionListener interface.
         */

        // The builder returns a server which you can use for several things
        HydraServer server = new Server.Builder("localhost", 8888, new ExampleServerProtocol())
                .bossThreads(2)
                .workerThreads(4)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .addListener(new HydraSessionListener() {
                    @Override
                    public void onConnected(Session session) {
                        LOGGER.log(Level.INFO, "%nClient connected!");
                    }

                    @Override
                    public void onDisconnected(Session session) {
                        LOGGER.log(Level.INFO, "%nClient disconnected!");
                    }
                })
                .build();

        // Check if server is actively running (not obligatory)
        if (server.isActive()) {
            LOGGER.log(Level.INFO, "Server is online!");
            // Returns the local address of the server that was set in the constructor
            LOGGER.log(Level.INFO, String.format("Socket address: %s%n", server.getLocalAddress()));
        }

        // As soon as a channel with a client is initialized it is added to the set of sessions
        // If no clients are connected the set is empty
        LOGGER.log(Level.INFO, "Sessions: %s".formatted(server.getSessions()));

        // Closes the server and releases the occupied resources
        //server.close();
    }
}