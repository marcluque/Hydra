package de.datasecs.hydra.example.server.serialization;

import de.datasecs.hydra.example.shared.SampleProtocol;
import de.datasecs.hydra.server.HydraServer;
import de.datasecs.hydra.server.Server;
import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.handler.listener.HydraSessionListener;
import io.netty.channel.ChannelOption;

/**
 * Created with love by DataSecs on 12.02.18
 */
public class ExampleSerializationServer {

    public static void main(String[] args) {
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

        if (server.isActive()) {
            System.out.println("Server is online!");
            // Returns the local address of the server that was set in the constructor
            System.out.printf("Socket address: %s%n", server.getLocalAdress());
        }
    }
}