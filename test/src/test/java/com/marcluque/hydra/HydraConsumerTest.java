package com.marcluque.hydra;

import com.marcluque.hydra.client.tcp.TCPClient;
import com.marcluque.hydra.client.tcp.HydraTCPClient;
import com.marcluque.hydra.client.TestClientProtocol;
import com.marcluque.hydra.server.HydraServer;
import com.marcluque.hydra.server.Server;
import com.marcluque.hydra.server.TestServerProtocol;
import com.marcluque.hydra.shared.ArrayPacket;
import com.marcluque.hydra.shared.FinishedPacket;
import com.marcluque.hydra.shared.Logger;
import com.marcluque.hydra.shared.TestPacket;
import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.handler.listener.HydraSessionListener;
import io.netty.channel.ChannelOption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class HydraConsumerTest {

    private static HydraServer server;

    private static HydraTCPClient client;

    private static boolean clientConnected;

    public static long[] measures = new long[7];

    public static long globalStart;

    public static long globalEnd;

    @Test
    void testAll() {
        System.out.println("---------------------------");
        System.out.println("Testing consumer functionality");
        System.out.println("---------------------------%n");

        // Phase 1 (epoll=false, connectAfterSetup=false)
        Logger.logInfo("Starting phase 1...");
        init(false, false);
        testServer();
        testClient(1);
        shutdown();
        HydraBasicTest.phaseFinished = false;
        clientConnected = false;

        Logger.printMetrics(measures);

        // Phase 2 (epoll=false, connectAfterSetup=true)
        System.out.printf("%n%n");
        Logger.logInfo("Starting phase 2...");
        init(false, true);
        testServer();
        testClient(2);
        shutdown();
        HydraBasicTest.phaseFinished = false;
        clientConnected = false;
        Logger.printMetrics(measures);

        // Phase 3 (epoll=true, connectAfterSetup=true)
        System.out.printf("%n%n");
        Logger.logInfo("Starting phase 3...");
        init(true, true);
        testServer();
        testClient(3);
        shutdown();
        Logger.printMetrics(measures);
    }

    public static void init(boolean connectAfterSetup, boolean epoll) {
        server = new Server.Builder("localhost", 8888, new TestServerProtocol())
                .useEpoll(epoll)
                .bossThreads(4)
                .workerThreads(2)
                .option(ChannelOption.SO_BACKLOG, 200)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .onConnected(c -> {
                    // Measure time the client takes to connect to server
                    globalEnd = System.nanoTime();
                    measures[1] = globalEnd - globalStart;

                    Logger.logDebug("TestClient connected!");

                    synchronized(HydraBasicTest.LOCK) {
                        clientConnected = true;
                        HydraBasicTest.LOCK.notify();
                    }
                })
                .onDisconnected(c -> Logger.logDebug("TestClient disconnected!"))
                .build();

        Logger.logDebug("Server started successfully!");

        client = new TCPClient.TCPClientBuilder("localhost", 8888, new TestClientProtocol())
                .useEpoll(epoll)
                .connectAfterSetup(connectAfterSetup)
                .workerThreads(4)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .addSessionListener(new HydraSessionListener() {
                    @Override
                    public void onConnected(Session session) {
                        Logger.logDebug("Connected to server!");
                    }

                    @Override
                    public void onDisconnected(Session session) {
                        Logger.logDebug("Disconnected from server!");
                    }
                })
                .build();
        // Measure time the client takes to connect to server
        globalStart = System.nanoTime();

        if (!connectAfterSetup) {
            Assertions.assertThrows(IllegalStateException.class, client::isConnected);
            client.connect();
            globalStart = System.nanoTime();
        }

        Logger.logDebug("Client started successfully!");
    }

    public static void testServer() {
        Assertions.assertTrue(server.isActive());
        Assertions.assertTrue(server.getChannel().isWritable());
        Assertions.assertTrue(server.getChannel().isOpen());
        Assertions.assertEquals("/127.0.0.1:8888", server.getLocalAddress().toString());

        // It's necessary to wait until Netty built the connection up entirely
        try {
            synchronized(HydraBasicTest.LOCK) {
                while(!clientConnected) {
                    HydraBasicTest.LOCK.wait();
                }

                // When the connection is established, there is one session registered
                Assertions.assertEquals(1, server.getSessions().size());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            // Restore interrupted state...
            Thread.currentThread().interrupt();
        }
    }

    public static void testClient(int phase) {
        Assertions.assertTrue(client.isConnected());
        Assertions.assertTrue(client.getChannel().isWritable());
        Assertions.assertTrue(client.getChannel().isOpen());
        Assertions.assertEquals("localhost/127.0.0.1:8888", client.getRemoteAddress().toString());
        Assertions.assertNotNull(client.getLocalAddress());
        Assertions.assertFalse(client.getWorkerGroup().isTerminated());
        Assertions.assertThrows(IllegalStateException.class, client::connect);
        Assertions.assertNotNull(client.getSession());


        /*
         * String test (number = 0)
         */
        client.send(new TestPacket(0, "Test"));
        Logger.logInfo(String.format("Phase %d: Test 0", phase));


        /*
         * Send 1000 Packets (number = 1)
         */
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            client.send(new TestPacket(1, "Test" + i));
        }
        long end = System.nanoTime();
        measures[2] = end - start;
        Logger.logInfo(String.format("Phase %d: Test 1", phase));


        /*
         * (String) list test (number = 2)
         */
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(String.format("test%d", i));
        }
        start = System.nanoTime();
        client.send(new TestPacket(2, list));
        end = System.nanoTime();
        measures[3] = end - start;
        Logger.logInfo(String.format("Phase %d: Test 2", phase));


        /*
         * (String) array test
         */
        String[] testArray = new String[1000];
        for (int i = 0; i < 1000; i++) {
            testArray[i] = String.format("test%d", i);
        }
        start = System.nanoTime();
        client.send(new ArrayPacket(testArray));
        end = System.nanoTime();
        measures[4] = end - start;
        Logger.logInfo(String.format("Phase %d: Array Test", phase));


        /*
         * Finish test
         */
        client.send(new FinishedPacket(phase));


        // When "okay" is received, test is finished
        try {
            synchronized(HydraBasicTest.LOCK) {
                while(!HydraBasicTest.phaseFinished) {
                    HydraBasicTest.LOCK.wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            // Restore interrupted state...
            Thread.currentThread().interrupt();
        }
    }

    public static void shutdown() {
        // Client
        long start = System.nanoTime();
        Future<?> closingFuture = client.close();
        try {
            closingFuture.get();
            long end = System.nanoTime();
            measures[5] = end - start;
            Logger.logDebug("Client shut down!");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            // Restore interrupted state...
            Thread.currentThread().interrupt();
        }

        Assertions.assertFalse(client.isConnected());
        Assertions.assertFalse(client.getChannel().isWritable());
        Assertions.assertFalse(client.getChannel().isOpen());
        Assertions.assertThrows(IllegalStateException.class, client::connect);
        Assertions.assertNull(client.getSession());
        Assertions.assertTrue(client.getWorkerGroup().isTerminated());


        // Server
        start = System.nanoTime();
        Future<?>[] closingFutures = server.close();
        try {
            for (int i = 0; i < closingFutures.length; i++) {
                Future<?> future = closingFutures[i];
                future.get();
                long end = System.nanoTime();
                measures[6] = end - start;
                Logger.logDebug("Server thread " + i + " shut down!");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            // Restore interrupted state...
            Thread.currentThread().interrupt();
        }

        Assertions.assertFalse(server.isActive());
        Assertions.assertFalse(server.getChannel().isWritable());
        Assertions.assertFalse(server.getChannel().isOpen());
        Assertions.assertTrue(server.getWorkerGroup().isTerminated());
        Assertions.assertTrue(server.getBossGroup().isTerminated());
    }
}
