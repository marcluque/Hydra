package de.datasecs.hydra.server;

import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.handler.listener.HydraSessionListener;
import io.netty.channel.ChannelOption;
import org.junit.BeforeClass;

/**
 * Created with love by DataSecs on 08.06.18
 */
public class HydraServerTest {

    private static HydraServer server;

    @BeforeClass
    public static void initServer() {
        server = new Server.Builder("localhost", 8888, )
                .onConnected(System.out::println)
                .onDisconnected(System.out::println)
                .addListener(new HydraSessionListener() {
                    @Override
                    public void onConnected(Session session) {

                    }

                    @Override
                    public void onDisconnected(Session session) {

                    }
                })
                .option(ChannelOption.TCP_NODELAY, true)
    }
}