package com.marcluque.hydra.client.udp;

import com.marcluque.hydra.client.HydraAbstractClientBuilder;
import com.marcluque.hydra.shared.handler.impl.UDPSession;
import com.marcluque.hydra.shared.protocol.Protocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.AttributeKey;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UDPClient {

    private static final Logger LOGGER = LogManager.getLogger(UDPClient.class.getName());

    private UDPClient() {}

    public static class UDPClientBuilder extends HydraAbstractClientBuilder<HydraUDPClient> {

        public UDPClientBuilder(int port, Protocol protocol) {
            super(port, protocol);
        }

        /**
         * Builds the final client. Returns an instance of type {@link HydraUDPClient}, not of type {@link UDPClient},
         * as {@link HydraUDPClient} includes all the useful methods for users.
         *
         * @return Returns an instance of {@link HydraUDPClient} that can, e.g.,
         * be used to get the session created for the client and server.
         */
        @Override
        public HydraUDPClient build() {
            boolean epoll = useEpoll && Epoll.isAvailable();
            EventLoopGroup workerGroup = epoll ? new EpollEventLoopGroup(workerThreads) : new NioEventLoopGroup(workerThreads);

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioDatagramChannel.class);

            //noinspection unchecked
            options.forEach((option, value) -> bootstrap.option((ChannelOption<? super Object>) option, value));
            //noinspection unchecked
            attributeKeys.forEach((key, value) -> bootstrap.attr((AttributeKey<? super Object>) key, value));

            UDPSession session = new UDPSession(protocol, false);
            bootstrap.handler(session);
            Channel channel = null;
            try {
                if (host != null) {
                    channel = bootstrap.bind(host, port).sync().channel();
                } else {
                    channel = bootstrap.bind(port).sync().channel();
                }
            } catch (InterruptedException e) {
                LOGGER.log(Level.ERROR, e);
                // Restore interrupted state...
                Thread.currentThread().interrupt();
            }
            session.setChannel(channel);

            return new HydraUDPClient(channel, workerGroup, session);
        }
    }
}
