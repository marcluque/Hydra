package com.marcluque.hydra.server;

import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.handler.impl.HydraSession;
import com.marcluque.hydra.shared.handler.listener.HydraSessionConsumer;
import com.marcluque.hydra.shared.handler.listener.HydraSessionListener;
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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created with love by marcluque on 29.11.17
 */
public class Server {

    private static final Logger LOGGER = LogManager.getLogger(Server.class.getName());

    public static class Builder {

        private final String host;

        private final int port;

        private int workerThreads = 2;

        private int bossThreads = 1;

        private final Map<ChannelOption<?>, Object> options = new HashMap<>();

        private final Map<ChannelOption<?>, Object> childOptions = new HashMap<>();

        private final Map<AttributeKey<?>, Object> attributeKeys = new HashMap<>();

        private final Map<AttributeKey<?>, Object> childAttributeKeys = new HashMap<>();

        private boolean useEpoll;

        private final Protocol protocol;

        private final HydraSessionConsumer hydraSessionConsumer;

        public Builder(String host, int port, Protocol protocol) {
            this.host = host;
            this.port = port;
            this.protocol = protocol;

            hydraSessionConsumer = new HydraSessionConsumer();
            protocol.addSessionConsumer(hydraSessionConsumer);
        }

        /**
         * Sets the number of worker threads for the server.
         * A worker thread performs non-blocking operations for one or more channels.
         * The standard amount is set to 2.
         *
         * @param workerThreads the amount of worker threads for the server
         */
        public Builder workerThreads(int workerThreads) {
            this.workerThreads = workerThreads;
            return this;
        }

        /**
         * Sets the number of boss threads for the server.
         * The standard number is set to 1.
         * Netty sets the standard amount to twice the amount of processors/cores.
         *
         * @param bossThreads the amount of boss threads for the server
         */
        public Builder bossThreads(int bossThreads) {
            this.bossThreads = bossThreads;
            return this;
        }

        /**
         * Adds a specific option to the server that is added to the channel configuration.
         * These options include a lot of possibilities.
         *
         * @param channelOption the desired channel option
         * @param value the value that is supposed to be set for the desired channel option
         *
         * @see <a href="https://netty.io/4.1/api/io/netty/channel/ChannelOption.html">channel options</a>
         */
        public <T> Builder option(ChannelOption<T> channelOption, T value) {
            options.put(channelOption, value);
            return this;
        }

        /**
         * Adds a specific option to the connections that are opened with the server's channel.
         *
         * @param channelOption the desired channel option
         * @param value the value that is supposed to be set for the desired channel option
         */
        public <T> Builder childOption(ChannelOption<T> channelOption, T value) {
            childOptions.put(channelOption, value);
            return this;
        }

        /**
         * Adds a specific attribute to the server. The attributes are saved in an attribute map by Netty.
         *
         * @param attributeKey the attribute key that is supposed to be stored in the map.
         * @param value the value that is supposed to be mapped to the given attribute key.
         */
        public <T> Builder attribute(AttributeKey<T> attributeKey, T value) {
            attributeKeys.put(attributeKey, value);
            return this;
        }

        /**
         * Adds a specific child attribute to the server. Child attributes apply to the connections that the server creates,
         * just like child options. The attributes are saved in an attribute map by Netty.
         *
         * @param attributeKey the attribute key that is supposed to be stored in the map.
         * @param value the value that is supposed to be mapped to the given attribute key.
         */
        public <T> Builder childAttribute(AttributeKey<T> attributeKey, T value) {
            childAttributeKeys.put(attributeKey, value);
            return this;
        }

        /**
         * Basically epoll decides whether it's a unix based system netty is operating on, or not.
         * This method gives the possibility to allow the usage of epoll, if it's available.
         *
         * @param useEpoll sets whether epoll should be used or not
         */
        public Builder useEpoll(boolean useEpoll) {
            this.useEpoll = useEpoll;
            return this;
        }

        /**
         * This method adds a session listener to the client. The session listener has 2 methods.
         * The first one is 'onConnected', which gets triggered when the client is successfully connected to the aimed server.
         * The second on is 'onDisconnected', which gets fired when the client is disconnected from the server it was
         * connected to.
         *
         * @param sessionListener this method takes an instance of the session listener interface HydraSessionListener.
         */
        public Builder addListener(HydraSessionListener sessionListener) {
            protocol.addSessionListener(sessionListener);
            return this;
        }

        public Builder onConnected(Consumer<Session> onConnectedConsumer) {
            hydraSessionConsumer.setOnConnectedConsumer(onConnectedConsumer);
            return this;
        }

        public Builder onDisconnected(Consumer<Session> onDisconnectedConsumer) {
            hydraSessionConsumer.setOnDisconnectedConsumer(onDisconnectedConsumer);
            return this;
        }

        /**
         * Builds the final server. Returns an instance of type HydraServer, not of type Server, as HydraServer includes
         * all the useful methods for users.
         *
         * @return Returns an instance of HydraServer that can e.g. be used to get the session created for the server and client.
         */
        public HydraServer build() {
            return setUpServer();
        }

        private HydraServer setUpServer() {
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

            return new HydraServer(channel, protocol, new EventLoopGroup[]{bossGroup, workerGroup});
        }
    }
}