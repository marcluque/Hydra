package com.marcluque.hydra.client;

import com.marcluque.hydra.HydraBasicTest;
import com.marcluque.hydra.shared.FinishedPacket;
import com.marcluque.hydra.shared.Logger;
import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.protocol.packets.StandardPacket;
import com.marcluque.hydra.shared.protocol.packets.listener.HydraPacketListener;
import com.marcluque.hydra.shared.protocol.packets.listener.PacketHandler;
import org.junit.jupiter.api.Assertions;

/**
 * Created by marcluque on 28.03.2019.
 */
public class TestClientPacketListener implements HydraPacketListener {

    public TestClientPacketListener() {}

    @PacketHandler
    public void onFinishedPacket(FinishedPacket finishedPacket, Session session) {
        Assertions.assertTrue(session.isConnected());
        Logger.logSuccess(String.format("Phase %d done!", finishedPacket.getNumber()));

        synchronized (HydraBasicTest.LOCK) {
            HydraBasicTest.phaseFinished = true;
            HydraBasicTest.LOCK.notify();
        }
    }

    @PacketHandler
    public void onStandardPacket(StandardPacket standardPacket, Session session) {
        Assertions.assertTrue(session.isConnected());
        Assertions.assertEquals("#Received!", standardPacket.toString());
    }
}