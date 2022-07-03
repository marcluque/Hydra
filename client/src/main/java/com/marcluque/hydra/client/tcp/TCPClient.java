package com.marcluque.hydra.client.tcp;

import com.marcluque.hydra.client.HydraAbstractClientBuilder;
import com.marcluque.hydra.shared.initializer.HydraChannelInitializer;
import com.marcluque.hydra.shared.protocol.Protocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created with love by marcluque on 30.11.17.
 */
public class TCPClient {

    private static final Logger LOGGER = LogManager.getLogger(TCPClient.class.getName());

    private TCPClient() {}

    public static class TCPClientBuilder extends HydraAbstractClientBuilder<HydraTCPClient> {

        public TCPClientBuilder(String host, int port, Protocol protocol) {
            super(port, protocol);
            super.host = host;
        }

        /**
         * Builds the final client. Returns an instance of type {@link HydraTCPClient}, not of type {@link TCPClient},
         * as {@link HydraTCPClient} includes all the useful methods for users.
         *
         * @return Returns an instance of {@link HydraTCPClient} that can, e.g.,
         * be used to get the session created for the client and server.
         */
        @Override
        public HydraTCPClient build() {
            boolean epoll = useEpoll && Epoll.isAvailable();
            EventLoopGroup workerGroup = epoll ? new EpollEventLoopGroup(workerThreads) : new NioEventLoopGroup(workerThreads);

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(epoll ? EpollSocketChannel.class : NioSocketChannel.class);
            bootstrap.group(workerGroup).remoteAddress(host, port);

            //noinspection unchecked
            options.forEach((option, value) -> bootstrap.option((ChannelOption<? super Object>) option, value));
            //noinspection unchecked
            attributeKeys.forEach((key, value) -> bootstrap.attr((AttributeKey<? super Object>) key, value));

            Channel channel = null;
            try {
                if (connectAfterSetup) {
                    bootstrap.handler(new HydraChannelInitializer<SocketChannel>(protocol, false));
                    channel = bootstrap.connect().sync().channel();
                }
            } catch (InterruptedException e) {
                LOGGER.log(Level.ERROR, e);
                // Restore interrupted state...
                Thread.currentThread().interrupt();
            }

            return connectAfterSetup ? new HydraTCPClient(channel, protocol, workerGroup) : new HydraTCPClient(protocol, workerGroup, bootstrap);
        }
    }
}