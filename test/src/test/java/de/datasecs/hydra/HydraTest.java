package de.datasecs.hydra;

import de.datasecs.hydra.client.Client;
import de.datasecs.hydra.client.HydraClient;
import de.datasecs.hydra.server.HydraServer;
import de.datasecs.hydra.server.Server;
import de.datasecs.hydra.shared.TestProtocol;
import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.handler.listener.HydraSessionListener;
import io.netty.channel.ChannelOption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Created by DataSec on 28.03.2019.
 */
public class HydraTest {

    private static HydraServer server;

    private static HydraClient client;

    @BeforeAll
    public static void init() {
        server = new Server.Builder("localhost", 8888, new TestProtocol())
                .addListener(new HydraSessionListener() {
                    @Override
                    public void onConnected(Session session) {
                        System.out.println("TestClient connected!");
                    }

                    @Override
                    public void onDisconnected(Session session) {
                        System.out.println("TestClient disconnected!");
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 10)
                .build();

        System.out.println("Server started!");

        client = new Client.Builder("localhost", 8888, new TestProtocol())
                .workerThreads(4)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .addSessionListener(new HydraSessionListener() {
                    @Override
                    public void onConnected(Session session) {
                        System.out.println("Connected to server!");
                    }

                    @Override
                    public void onDisconnected(Session session) {
                        System.out.println("\nDisconnected from server!");
                    }
                })
                .build();

        System.out.println("Client started!");
    }

    @Test
    public void testServer() {
        Assertions.assertTrue(server.isActive());
        Assertions.assertEquals(0, server.getSessions().size());
    }

    @Test
    public void testClient() {
        Assertions.assertTrue(client.isConnected());
        Assertions.assertThrows(IllegalStateException.class, client::connect);
    }
}
