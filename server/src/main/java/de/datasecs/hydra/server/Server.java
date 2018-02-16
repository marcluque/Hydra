package de.datasecs.hydra.server;

import de.datasecs.hydra.shared.handler.listener.HydraSessionListener;
import de.datasecs.hydra.shared.initializer.HydraChannelInitializer;
import de.datasecs.hydra.shared.protocol.HydraProtocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with love by DataSecs on 29.11.17
 */
public class Server {

    public static class Builder {

        private String host;

        private int port;

        private int workerThreads = 2;

        private int bossThreads = 1;

        private Map<ChannelOption, Object> options = new HashMap<>();

        private Map<ChannelOption, Object> childOptions = new HashMap<>();

        private Map<AttributeKey, Object> attributeKeys = new HashMap<>();

        private Map<AttributeKey, Object> childAttributeKeys = new HashMap<>();

        private boolean useEpoll;

        private HydraProtocol protocol;

        public Builder(String host, int port, HydraProtocol protocol) {
            this.host = host;
            this.port = port;
            this.protocol = protocol;
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
         * @see <a href="https://netty.io/4.1/api/io/netty/channel/ChannelOption.html">channel options</a>
         *
         * @param channelOption the desired channel option
         * @param value the value that is supposed to be set for the desired channel option
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
            EventLoopGroup workerGroup, bossGroup;
            boolean epoll = useEpoll && Epoll.isAvailable();

            EventLoopGroup[] loopGroups = new EventLoopGroup[]{bossGroup = epoll ? new EpollEventLoopGroup(bossThreads) : new NioEventLoopGroup(bossThreads),
                    workerGroup = epoll ? new EpollEventLoopGroup(workerThreads) : new NioEventLoopGroup(workerThreads)};

            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(epoll ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .childHandler(new HydraChannelInitializer(protocol, true));

            options.forEach(serverBootstrap::option);
            childOptions.forEach(serverBootstrap::childOption);

            attributeKeys.forEach(serverBootstrap::attr);
            attributeKeys.forEach(serverBootstrap::childAttr);

            Channel channel = null;
            try {
                channel = serverBootstrap.bind(host, port).sync().channel();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return new HydraServer(channel, protocol, loopGroups);
        }
    }
}