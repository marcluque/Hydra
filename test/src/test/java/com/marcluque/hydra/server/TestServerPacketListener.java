package com.marcluque.hydra.server;

import com.marcluque.hydra.shared.ArrayPacket;
import com.marcluque.hydra.shared.FinishedPacket;
import com.marcluque.hydra.shared.TestPacket;
import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.protocol.packets.StandardPacket;
import com.marcluque.hydra.shared.protocol.packets.listener.HydraPacketListener;
import com.marcluque.hydra.shared.protocol.packets.listener.PacketHandler;
import com.marcluque.hydra.shared.serialization.CustomClass;
import com.marcluque.hydra.shared.serialization.CustomClassExtended;
import com.marcluque.hydra.shared.serialization.SerializationPacket;
import org.junit.jupiter.api.Assertions;

import java.util.*;

/**
 * Created by marcluque on 28.03.2019.
 */
public class TestServerPacketListener implements HydraPacketListener {

    private final String[] testArray = new String[1000];

    private final List<String> testList = new ArrayList<>();

    private final CustomClass testCustomClass;

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

        CustomClassExtended customClassExtended = new CustomClassExtended("testStringExtended",
                UUID.fromString("1ce41de2-659e-4949-9482-c5de92c2ad6c"),
                Long.MAX_VALUE,
                String.class, null);

        testCustomClass = new CustomClass("testString",
                Integer.MAX_VALUE,
                testArray,
                Arrays.asList(testArray),
                new Vector<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)),
                customClassExtended);
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

    private void assertCustomClassEquality(CustomClass expectedCustomClass, CustomClass actualCustomClass) {
        Assertions.assertEquals(expectedCustomClass.getTestString(), actualCustomClass.getTestString());
        Assertions.assertEquals(expectedCustomClass.getTestInt(), actualCustomClass.getTestInt());
        Assertions.assertArrayEquals(expectedCustomClass.getTestStringArray(), actualCustomClass.getTestStringArray());
        Assertions.assertEquals(expectedCustomClass.getTestStringList().toArray(),
                actualCustomClass.getTestStringList().toArray());
        Assertions.assertEquals(expectedCustomClass.getTestObject(), actualCustomClass.getTestObject());

        CustomClassExtended expectedCustomClassExtended = expectedCustomClass.getCustomClassExtended();
        CustomClassExtended actualCustomClassExtended = actualCustomClass.getCustomClassExtended();
        Assertions.assertEquals(expectedCustomClassExtended.getTestStringExtended(),
                actualCustomClassExtended.getTestStringExtended());
        Assertions.assertEquals(expectedCustomClassExtended.getTestUUID(), actualCustomClassExtended.getTestUUID());
        Assertions.assertEquals(expectedCustomClassExtended.getTestLong(), actualCustomClassExtended.getTestLong());
        Assertions.assertEquals(expectedCustomClassExtended.getTestClass(), actualCustomClassExtended.getTestClass());
    }

    @SuppressWarnings("unused") // Methods annotated with @PacketHandler are called at runtime by Hydra
    @PacketHandler
    public void onSerializationPacket(SerializationPacket serializationPacket, Session session) {
        Assertions.assertTrue(session.isConnected());

        CustomClass receivedCustomClass = serializationPacket.getCustomClass();
        assertCustomClassEquality(testCustomClass, receivedCustomClass);

        CustomClass[] receivedCustomClasses = serializationPacket.getCustomClasses();
        for (CustomClass customClass : receivedCustomClasses) {
            assertCustomClassEquality(testCustomClass, customClass);
        }
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