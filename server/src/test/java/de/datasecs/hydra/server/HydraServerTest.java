package de.datasecs.hydra.server;

import io.netty.channel.ChannelOption;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created with love by DataSecs on 08.06.18
 */
public class HydraServerTest {

    private static HydraServer server;

    @BeforeClass
    public static void initServer() {
        server = new Server.Builder("localhost", 8888, new TestProtocol())
                .bossThreads(2)
                .workerThreads(4)
                .onConnected(session -> System.out.println("TestClient connected!"))
                .onDisconnected(session -> System.out.println("TestClient disconnected!"))
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_BACKLOG, 200)
                .build();
    }

    @Test
    public void testServer() {
        Assert.assertTrue(server.isActive());
    }
}