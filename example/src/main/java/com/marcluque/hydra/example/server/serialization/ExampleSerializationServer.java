package com.marcluque.hydra.example.server.serialization;

import com.marcluque.hydra.server.tcp.HydraTCPServer;
import com.marcluque.hydra.server.tcp.TCPServer;
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
        HydraTCPServer server = new TCPServer.Builder("localhost", 8888, new ExampleSerializationServerProtocol())
                .bossThreads(2)
                .workerThreads(4)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .addListener(new HydraSessionListener() {
                    @Override
                    public void onConnected(Session session) {
                        LOGGER.log(Level.INFO, "%nClient connected!%n");
                    }

                    @Override
                    public void onDisconnected(Session session) {
                        LOGGER.log(Level.INFO, "%nClient disconnected!%n");
                    }
                })
                .build();

        if (server.isActive()) {
            LOGGER.log(Level.INFO, "Server is online!%n");
            // Returns the local address of the server that was set in the constructor
            LOGGER.log(Level.INFO, "Socket address: {}%n", server.getLocalAddress());
        }
    }
}