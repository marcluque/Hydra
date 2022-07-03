package com.marcluque.hydra.example.server.serialization;

import com.marcluque.hydra.server.HydraServer;
import com.marcluque.hydra.server.Server;
import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.handler.listener.HydraSessionListener;
import io.netty.channel.ChannelOption;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created with love by marcluque on 12.02.18
 */
public class ExampleSerializationServer {

    private static final Logger LOGGER = LogManager.getLogger(ExampleSerializationServer.class.getName());

    public static void main(String[] args) {
        HydraServer server = new Server.Builder("localhost", 8888, new ExampleSerializationServerProtocol())
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

        if (server.isActive()) {
            LOGGER.log(Level.INFO, "Server is online!");
            // Returns the local address of the server that was set in the constructor
            LOGGER.log(Level.INFO, String.format("Socket address: %s%n", server.getLocalAddress()));
        }
    }
}