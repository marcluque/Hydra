package com.marcluque.hydra.server;

import com.marcluque.hydra.shared.ArrayPacket;
import com.marcluque.hydra.shared.FinishedPacket;
import com.marcluque.hydra.shared.TestPacket;
import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.protocol.packets.StandardPacket;
import com.marcluque.hydra.shared.protocol.packets.listener.HydraPacketListener;
import com.marcluque.hydra.shared.protocol.packets.listener.PacketHandler;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcluque on 28.03.2019.
 */
public class TestServerPacketListener implements HydraPacketListener {

    private final String[] testArray = new String[1000];

    private final List<String> testList = new ArrayList<>();

    private int packetCounter = 0;

    public TestServerPacketListener() {
        // Create test array
        for (int i = 0; i < 1000; i++) {
            testArray[i] = String.format("test%d", i);
        }

        // Create test list
        for (int i = 0; i < 1000; i++) {
            testList.add(String.format("test%d", i));
        }
    }

    @SuppressWarnings("unused") // Methods annotated with @PacketHandler are called at runtime by Hydra
    @PacketHandler
    public void onTestPacket(TestPacket testPacket, Session session) {
        Assertions.assertTrue(session.isConnected());
        switch (testPacket.getNumber()) {
            case 0 -> Assertions.assertEquals("Test", testPacket.getObject().toString());
            case 1 -> {
                Assertions.assertEquals("Test" + packetCounter, testPacket.getObject().toString());
                packetCounter++;
            }
            case 2 -> Assertions.assertIterableEquals(testList, (Iterable<?>) testPacket.getObject());
        }

        session.send(new StandardPacket("#Received!"));
    }

    @SuppressWarnings("unused") // Methods annotated with @PacketHandler are called at runtime by Hydra
    @PacketHandler
    public void onArrayPacket(ArrayPacket arrayPacket, Session session) {
        Assertions.assertTrue(session.isConnected());
        Assertions.assertArrayEquals(testArray, arrayPacket.getStrings());
    }

    @SuppressWarnings("unused") // Methods annotated with @PacketHandler are called at runtime by Hydra
    @PacketHandler
    public void onFinishedPacket(FinishedPacket finishedPacket, Session session) {
        Assertions.assertTrue(session.isConnected());
        session.send(new FinishedPacket(finishedPacket.getNumber()));
    }

    @SuppressWarnings("unused") // Methods annotated with @PacketHandler are called at runtime by Hydra
    @PacketHandler
    public void onStandardPacket(StandardPacket standardPacket, Session session) {
        Assertions.assertTrue(session.isConnected());
        session.send(new StandardPacket("#Received!"));
    }
}