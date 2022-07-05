package com.marcluque.hydra;

import com.marcluque.hydra.client.TestClientProtocol;
import com.marcluque.hydra.client.tcp.HydraTCPClient;
import com.marcluque.hydra.client.tcp.TCPClient;
import com.marcluque.hydra.server.TestServerProtocol;
import com.marcluque.hydra.server.tcp.HydraTCPServer;
import com.marcluque.hydra.server.tcp.TCPServer;
import com.marcluque.hydra.shared.*;
import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.handler.listener.HydraSessionListener;
import com.marcluque.hydra.shared.protocol.Protocol;
import com.marcluque.hydra.shared.serialization.CustomClass;
import com.marcluque.hydra.shared.serialization.CustomClassExtended;
import com.marcluque.hydra.shared.serialization.SerializationPacket;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.util.ResourceLeakDetector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by marcluque on 28.03.2019.
 */
public class HydraBasicTCPTest {

    private static HydraTCPServer server;

    private static HydraTCPClient client;

    private static Protocol clientProtocol;

    public static final Object LOCK = new Object();

    private static boolean clientConnected;

    public static boolean phaseFinished;

    public static final List<Measurement> MEASUREMENTS = new ArrayList<>();

    public static long globalStart;

    public static long globalEnd;

    @Test
    void testAll() {
        System.out.println("---------------------------");
        System.out.println("Testing basic functionality");
        System.out.println("---------------------------");

        // Phase 1 (epoll=false, connectAfterSetup=false)
        Logger.logInfo("Starting phase 1...");
        init(false, false);
        testServer();
        testClient(1);
        shutdown();
        phaseFinished = false;
        clientConnected = false;
        Logger.flushMetrics(MEASUREMENTS);

        // Phase 2 (epoll=false, connectAfterSetup=true)
        System.out.printf("%n%n");
        Logger.logInfo("Starting phase 2...");
        init(false, true);
        testServer();
        testClient(2);
        shutdown();
        phaseFinished = false;
        clientConnected = false;
        Logger.flushMetrics(MEASUREMENTS);

        // Phase 3 (epoll=true, connectAfterSetup=true)
        System.out.printf("%n%n");
        Logger.logInfo("Starting phase 3...");
        init(true, true);
        testServer();
        testClient(3);
        shutdown();
        Logger.flushMetrics(MEASUREMENTS);
    }

    public static void init(boolean connectAfterSetup, boolean epoll) {
        server = new TCPServer.Builder("localhost", 8888, new TestServerProtocol())
                .useEpoll(epoll)
                .bossThreads(4)
                .workerThreads(2)
                .option(ChannelOption.SO_BACKLOG, 200)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .addListener(new HydraSessionListener() {
                    @Override
                    public void onConnected(Session session) {
                        // Measure time the client takes to connect to server
                        globalEnd = System.nanoTime();
                        var measurement = new Measurement("%s[MEASUREMENT] Connection client: %s%d ns = %d µs = %d ms%n",
                                globalEnd - globalStart);
                        MEASUREMENTS.add(measurement);

                        Logger.logDebug("TestClient connected!");

                        synchronized(LOCK) {
                            clientConnected = true;
                            LOCK.notify();
                        }
                    }

                    @Override
                    public void onDisconnected(Session session) {
                        Logger.logDebug("TestClient disconnected!");
                    }
                })
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
            synchronized(LOCK) {
                while(!clientConnected) {
                    LOCK.wait();
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
        ChannelFuture channelFuture = client.send(new TestPacket(0, "Test"));
        try {
            channelFuture.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Logger.logInfo(String.format("Phase %d: Test 0", phase));


        /*
         * Send 1000 Packets (number = 1)
         */
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            ChannelFuture f = client.send(new TestPacket(1, "Test" + i));
            try {
                f.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        long end = System.nanoTime();
        var measurement = new Measurement("%s[MEASUREMENT] Packets (n=1000, size_packet=10 byte): %s%d ns = %d µs = %d ms%n",
                end - start);
        MEASUREMENTS.add(measurement);
        Logger.logInfo(String.format("Phase %d: Test 1", phase));


        /*
         * (String) list test (number = 2)
         */
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(String.format("test%d", i));
        }
        start = System.nanoTime();
        channelFuture = client.send(new TestPacket(2, list));
        try {
            channelFuture.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        end = System.nanoTime();
        measurement = new Measurement("%s[MEASUREMENT] String-List (n=1000, size_string=5 byte): %s%d ns = %d µs = %d ms%n",
                end - start);
        MEASUREMENTS.add(measurement);
        Logger.logInfo(String.format("Phase %d: Test 2", phase));


        /*
         * (String) array test
         */
        String[] testArray = new String[1000];
        for (int i = 0; i < 1000; i++) {
            testArray[i] = String.format("test%d", i);
        }
        start = System.nanoTime();
        channelFuture = client.send(new ArrayPacket(testArray));
        try {
            channelFuture.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        end = System.nanoTime();
        measurement = new Measurement("%s[MEASUREMENT] String-Array (n=1000, size_string=5 byte): %s%d ns = %d µs = %d ms%n",
                end - start);
        MEASUREMENTS.add(measurement);
        Logger.logInfo(String.format("Phase %d: Array Test", phase));


        /*
         * serialization test
         */
        CustomClassExtended customClassExtended = new CustomClassExtended("testStringExtended",
                UUID.fromString("1ce41de2-659e-4949-9482-c5de92c2ad6c"),
                Long.MAX_VALUE,
                String.class);
        for (int i = 0; i < 1000; i++) {
            testArray[i] = String.format("test%d", i);
        }
        CustomClass customClass = new CustomClass("testString",
                Integer.MAX_VALUE,
                testArray,
                Arrays.asList(testArray),
                new Vector<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)),
                customClassExtended);
        start = System.nanoTime();
        channelFuture = client.send(new SerializationPacket(customClass));
        try {
            channelFuture.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        end = System.nanoTime();
        measurement = new Measurement("%s[MEASUREMENT] Serialization of custom class: %s%d ns = %d µs = %d ms%n",
                end - start);
        MEASUREMENTS.add(measurement);
        Logger.logInfo(String.format("Phase %d: Serialization Test", phase));


        /*
         * Finish test
         */
        channelFuture = client.send(new FinishedPacket(phase));
        try {
            channelFuture.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        // When "okay" is received, test is finished
        try {
            synchronized(LOCK) {
                while(!phaseFinished) {
                    LOCK.wait();
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
            var measurement = new Measurement("%s[MEASUREMENT] Shutdown client: %s%d ns = %d µs = %d ms%n",
                    end - start);
            MEASUREMENTS.add(measurement);
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
                closingFutures[i].get();

                long end = System.nanoTime();
                var measurement = new Measurement("%s[MEASUREMENT] Shutdown server: %s%d ns = %d µs = %d ms%n",
                        end - start);
                MEASUREMENTS.add(measurement);
                Logger.logDebug("Server thread group " + i + " shut down!");
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