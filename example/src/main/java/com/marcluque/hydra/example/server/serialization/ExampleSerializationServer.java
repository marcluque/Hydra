package com.marcluque.hydra.example.server.serialization;

import com.marcluque.hydra.server.HydraServer;
import com.marcluque.hydra.server.Server;
import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.handler.listener.HydraSessionListener;
import io.netty.channel.ChannelOption;

/**
 * Created with love by marcluque on 12.02.18
 */
public class ExampleSerializationServer {

    public static void main(String[] args) {
        HydraServer server = new Server.Builder("localhost", 8888, new ExampleSerializationServerProtocol())
                .bossThreads(2)
                .workerThreads(4)
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

        if (server.isActive()) {
            System.out.println("Server is online!");
            // Returns the local address of the server that was set in the constructor
            System.out.printf("Socket address: %s%n", server.getLocalAdress());
        }
    }
}