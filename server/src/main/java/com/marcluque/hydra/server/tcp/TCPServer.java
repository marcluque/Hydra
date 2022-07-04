package com.marcluque.hydra.server.tcp;

import com.marcluque.hydra.server.AbstractServerBuilder;
import com.marcluque.hydra.shared.initializer.HydraChannelInitializer;
import com.marcluque.hydra.shared.protocol.Protocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created with love by marcluque on 29.11.17
 */
public class TCPServer {

    private static final Logger LOGGER = LogManager.getLogger(TCPServer.class.getName());

    private TCPServer() {}

    public static class Builder extends AbstractServerBuilder<HydraTCPServer> {

        public Builder(String host, int port, Protocol protocol) {
            super(port, protocol);
            super.host = host;
        }

        /**
         * Builds the final server. Returns an instance of type HydraServer, not of type Server, as HydraServer includes
         * all the useful methods for users.
         *
         * @return Returns an instance of HydraServer that can e.g. be used to get the session created for the server and client.
         */
        @Override
        public HydraTCPServer build() {
            boolean epoll = useEpoll && Epoll.isAvailable();
            EventLoopGroup workerGroup = epoll ? new EpollEventLoopGroup(workerThreads) : new NioEventLoopGroup(workerThreads);
            EventLoopGroup bossGroup = epoll ? new EpollEventLoopGroup(bossThreads) : new NioEventLoopGroup(bossThreads);

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(epoll ? EpollServerSocketChannel.class : NioServerSocketChannel.class);
            serverBootstrap.childHandler(new HydraChannelInitializer<SocketChannel>(protocol, true));
            serverBootstrap.group(bossGroup, workerGroup);

            //noinspection unchecked
            options.forEach((option, value) -> serverBootstrap.option((ChannelOption<? super Object>) option, value));
            //noinspection unchecked
            childOptions.forEach((childOption, value) -> serverBootstrap.childOption((ChannelOption<? super Object>) childOption, value));

            //noinspection unchecked
            attributeKeys.forEach((key, value) -> serverBootstrap.attr((AttributeKey<? super Object>) key, value));
            //noinspection unchecked
            childAttributeKeys.forEach((childKey, value) -> serverBootstrap.childAttr((AttributeKey<? super Object>) childKey, value));

            Channel channel = null;
            try {
                channel = serverBootstrap.bind(host, port).sync().channel();
            } catch (InterruptedException e) {
                LOGGER.log(Level.ERROR, e);
                // Restore interrupted state...
                Thread.currentThread().interrupt();
            }

            return new HydraTCPServer(channel, protocol, new EventLoopGroup[]{bossGroup, workerGroup});
        }
    }
}