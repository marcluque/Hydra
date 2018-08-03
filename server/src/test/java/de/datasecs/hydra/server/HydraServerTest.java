package de.datasecs.hydra.server;

import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.handler.listener.HydraSessionListener;
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
                .addListener(new HydraSessionListener() {
                    @Override
                    public void onConnected(Session session) {
                        System.out.println("TestClient connected!");
                    }

                    @Override
                    public void onDisconnected(Session session) {
                        System.out.println("TestClient disconnected!");
                    }
                })
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