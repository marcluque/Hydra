package com.marcluque.hydra.client;

import com.marcluque.hydra.shared.TestPacket;
import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.protocol.packets.StandardPacket;
import com.marcluque.hydra.shared.protocol.packets.listener.HydraPacketListener;
import com.marcluque.hydra.shared.protocol.packets.listener.PacketHandler;
import org.junit.jupiter.api.Assertions;

/**
 * Created by marcluque on 28.03.2019.
 */
public class TestClientPacketListener implements HydraPacketListener {

    public TestClientPacketListener() {

    }

    @PacketHandler
    public void onTestPacket(TestPacket examplePacket, Session session) {
        Assertions.assertTrue(session.isConnected());
        System.out.printf("Stage %d done!%n", examplePacket.getNumber());
    }

    @PacketHandler
    public void onStandardPacket(StandardPacket standardPacket, Session session) {
        Assertions.assertTrue(session.isConnected());
        Assertions.assertEquals("#Received!", standardPacket.getObject().toString());
    }
}