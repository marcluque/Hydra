package com.marcluque.hydra.server.udp;

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

import java.util.HashMap;
import java.util.Map;

public class UDPServer {

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

        private Protocol protocol;

        public Builder(int port, Protocol protocol) {
            this.port = port;
            this.protocol = protocol;
        }

        /**
         * If the socket has no wildcard address it can't receive broadcast packets.
         * You may set {@link ChannelOption} SO_BROADCAST to false and then use a host address.
         *
         * @param host host the server will be bound to.
         */
        public Builder host(String host) {
            this.host = host;
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
         * Builds the final server. Returns an instance of type HydraServer, not of type Server, as HydraServer includes
         * all the useful methods for users.
         *
         * @return Returns an instance of HydraServer that can e.g. be used to get the session created for the server and client.
         */
        public HydraUDPServer build() {
            boolean epoll = useEpoll && Epoll.isAvailable();
            EventLoopGroup group = epoll ? new EpollEventLoopGroup() : new NioEventLoopGroup();

            // TODO: Serverbootstrap possible?
            Bootstrap bootstrap = new Bootstrap();

            UDPSession session = new UDPSession(protocol, true);
            bootstrap.handler(session);
            bootstrap.group(group).channel(NioDatagramChannel.class);

            options.forEach(bootstrap::option);

            Channel channel = null;
            try {
                channel = host != null ? bootstrap.bind(host, port).sync().channel()
                                        : bootstrap.bind(port).sync().channel();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            session.setChannel(channel);
            return new HydraUDPServer(channel, group, session);
        }
    }
}
