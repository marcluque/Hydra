package de.datasecs.hydra.server;

import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.handler.listener.HydraSessionConsumer;
import de.datasecs.hydra.shared.handler.listener.HydraSessionListener;
import de.datasecs.hydra.shared.initializer.HydraChannelInitializer;
import de.datasecs.hydra.shared.protocol.Protocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.udt.UdtChannel;
import io.netty.channel.udt.nio.NioUdtProvider;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

/**
 * Created with love by DataSecs on 29.11.17
 */
public class Server {

    public static class Builder {

        private String host;

        private int port;

        private boolean useUDT;

        private boolean useUDP;

        private int workerThreads = 2;

        private int bossThreads = 1;

        private Map<ChannelOption, Object> options = new HashMap<>();

        private Map<ChannelOption, Object> childOptions = new HashMap<>();

        private Map<AttributeKey, Object> attributeKeys = new HashMap<>();

        private Map<AttributeKey, Object> childAttributeKeys = new HashMap<>();

        private boolean useEpoll;

        private Protocol protocol;

        private HydraSessionConsumer hydraSessionConsumer;

        public Builder(String host, int port, Protocol protocol) {
            this.host = host;
            this.port = port;
            this.protocol = protocol;

            hydraSessionConsumer = new HydraSessionConsumer();
            protocol.addSessionConsumer(hydraSessionConsumer);
        }

        /**
         * This attribute determines whether the server is supposed to use UDT (an modified version of UDP) as
         * data transfer protocol. The standard value is false.
         * @see <a href="https://en.wikipedia.org/wiki/UDP-based_Data_Transfer_Protocol">UDT</a>
         *
         * @param useUDT determines whether the server is supposed to use UDT (an modified version of UDP) as data transfer protocol
         */
        public Builder useUDT(boolean useUDT) {
            this.useUDT = useUDT;
            return this;
        }

        /**
         * This attribute determines whether the server is supposed to use UDP as data transfer protocol. The standard value is false.
         * @see <a href="https://en.wikipedia.org/wiki/User_Datagram_Protocol">UDP</a>
         *
         * @param useUDP determines whether the server is supposed to use UDP as data transfer protocol
         */
        public Builder useUDP(boolean useUDP) {
            this.useUDP = useUDP;
            return this;
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
            // TODO: Throw an error if more than one option is used

            EventLoopGroup workerGroup, bossGroup;
            boolean epoll = useEpoll && Epoll.isAvailable();
            ServerBootstrap serverBootstrap;
            Bootstrap bootstrap;

            if (useUDP) {
                bootstrap = new Bootstrap();
                EventLoopGroup group = new NioEventLoopGroup();
                bootstrap.group(group)
                        .channel(NioDatagramChannel.class)
                        .handler(new HydraChannelInitializer<NioDatagramChannel>(protocol, true, true));

                options.forEach(bootstrap::option);

                Channel channel = null;
                try {
                    channel = bootstrap.bind(host, port).sync().channel();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return new HydraServer(channel, protocol, new EventLoopGroup[]{group});
            } else {
                serverBootstrap = new ServerBootstrap();

                if (useUDT) {
                    ThreadFactory acceptFactory = new DefaultThreadFactory("accept");
                    ThreadFactory connectFactory = new DefaultThreadFactory("connect");
                    workerGroup = new NioEventLoopGroup(1, connectFactory, NioUdtProvider.MESSAGE_PROVIDER);
                    bossGroup = new NioEventLoopGroup(1, acceptFactory, NioUdtProvider.MESSAGE_PROVIDER);

                    // TODO: Handler needed?
                    serverBootstrap.channelFactory(NioUdtProvider.MESSAGE_ACCEPTOR);
                    serverBootstrap.childHandler(new HydraChannelInitializer<UdtChannel>(protocol, true, false));
                } else {
                    workerGroup = epoll ? new EpollEventLoopGroup(workerThreads) : new NioEventLoopGroup(workerThreads);
                    bossGroup = epoll ? new EpollEventLoopGroup(bossThreads) : new NioEventLoopGroup(bossThreads);

                    serverBootstrap.channel(epoll ? EpollServerSocketChannel.class : NioServerSocketChannel.class);
                    serverBootstrap.childHandler(new HydraChannelInitializer<SocketChannel>(protocol, true, false));
                }

                serverBootstrap.group(bossGroup, workerGroup);

                options.forEach(serverBootstrap::option);
                childOptions.forEach(serverBootstrap::childOption);

                attributeKeys.forEach(serverBootstrap::attr);
                childAttributeKeys.forEach(serverBootstrap::childAttr);

                Channel channel = null;
                try {
                    channel = serverBootstrap.bind(host, port).sync().channel();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return new HydraServer(channel, protocol, new EventLoopGroup[]{bossGroup, workerGroup});
            }
        }
    }
}