package de.datasecs.hydra;

import de.datasecs.hydra.client.Client;
import de.datasecs.hydra.client.HydraClient;
import de.datasecs.hydra.client.TestClientProtocol;
import de.datasecs.hydra.server.HydraServer;
import de.datasecs.hydra.server.Server;
import de.datasecs.hydra.server.TestServerProtocol;
import de.datasecs.hydra.shared.TestPacket;
import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.handler.listener.HydraSessionListener;
import de.datasecs.hydra.shared.protocol.packets.StandardPacket;
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

    private static final Object LOCK = new Object();

    private static boolean clientConnected;

    @Test
    public void testAll() {
        init();
        testServer();
        testClient();
    }

    public static void init() {
        server = new Server.Builder("localhost", 8888, new TestServerProtocol())
                .addListener(new HydraSessionListener() {
                    @Override
                    public void onConnected(Session session) {
                        System.out.println("TestClient connected!");
                        synchronized(LOCK) {
                            clientConnected = true;
                            LOCK.notify();
                        }
                    }

                    @Override
                    public void onDisconnected(Session session) {
                        System.out.println("TestClient disconnected!");
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 200)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .build();

        System.out.println("Server started successfully!");

        client = new Client.Builder("localhost", 8888, new TestClientProtocol())
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

        System.out.println("Client started successfully!");
    }

    public static void testServer() {
        Assertions.assertTrue(server.isActive());
        Assertions.assertTrue(server.getChannel().isWritable());
        Assertions.assertTrue(server.getChannel().isOpen());

        // It's necessary to wait until Netty built the connection up entirely
        try {
            synchronized(LOCK) {
                while(!clientConnected) {
                    LOCK.wait();
                }

                Assertions.assertEquals(1, server.getSessions().size());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void testClient() {
        Assertions.assertTrue(client.isConnected());
        Assertions.assertTrue(client.getChannel().isWritable());
        Assertions.assertTrue(client.getChannel().isOpen());
        Assertions.assertThrows(IllegalStateException.class, client::connect);
        Assertions.assertNotNull(client.getSession());

        // String test
        client.send(new TestPacket(1, "Test"));

        // (String) array test
        String[] testArray = new String[1000];
        for (int i = 0; i < 1000; i++) {
            testArray[i] = String.format("test%d", i);
        }
        client.send(new TestPacket(2, testArray));

        // Custom class test

        // Finish test, when okay is received, realize with synchronized
    }
}