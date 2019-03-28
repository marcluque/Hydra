package de.datasecs.hydra.server;

import de.datasecs.hydra.shared.TestPacket;
import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.protocol.packets.StandardPacket;
import de.datasecs.hydra.shared.protocol.packets.listener.HydraPacketListener;
import de.datasecs.hydra.shared.protocol.packets.listener.PacketHandler;
import org.junit.jupiter.api.Assertions;

/**
 * Created by DataSec on 28.03.2019.
 */
public class TestServerPacketListener implements HydraPacketListener {

    private String[] testArray = new String[1000];

    public TestServerPacketListener() {
        // Create test array
        for (int i = 0; i < 1000; i++) {
            testArray[i] = String.format("test%d", i);
        }
    }

    @PacketHandler
    public void onTestPacket(TestPacket examplePacket, Session session) {
        Assertions.assertTrue(session.isConnected());
        switch (examplePacket.getNumber()) {
            case 0:
                Assertions.assertEquals("Test", examplePacket.getObject().toString());
                break;
            case 1:
                Assertions.assertArrayEquals(testArray, (String[]) examplePacket.getObject());
                break;
            case 2:

                break;
        }

        session.send(new StandardPacket("#Received!"));
    }

    @PacketHandler
    public void onStandardPacket(StandardPacket standardPacket, Session session) {
        Assertions.assertTrue(session.isConnected());
        session.send(new StandardPacket("#Received!"));
    }
}
