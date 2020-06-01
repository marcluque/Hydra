package de.datasecs.hydra.client;

import de.datasecs.hydra.shared.TestPacket;
import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.protocol.packets.StandardPacket;
import de.datasecs.hydra.shared.protocol.packets.listener.HydraPacketListener;
import de.datasecs.hydra.shared.protocol.packets.listener.PacketHandler;
import org.junit.jupiter.api.Assertions;

/**
 * Created by DataSec on 28.03.2019.
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