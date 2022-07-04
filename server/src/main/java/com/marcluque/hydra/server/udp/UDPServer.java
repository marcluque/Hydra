package com.marcluque.hydra.server.udp;

import com.marcluque.hydra.server.AbstractServerBuilder;
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

public class UDPServer {

    private static final Logger LOGGER = LogManager.getLogger(UDPServer.class.getName());

    private UDPServer() {}

    public static class Builder extends AbstractServerBuilder<HydraUDPServer> {

        public Builder(int port, Protocol protocol) {
            super(port, protocol);
        }

        /**
         * Builds the final server. Returns an instance of type HydraServer, not of type Server, as HydraServer includes
         * all the useful methods for users.
         *
         * @return Returns an instance of HydraServer that can e.g. be used to get the session created for the server and client.
         */
        @Override
        public HydraUDPServer build() {
            boolean epoll = useEpoll && Epoll.isAvailable();
            EventLoopGroup group = epoll ? new EpollEventLoopGroup() : new NioEventLoopGroup();

            // TODO: Server-bootstrap possible?
            Bootstrap bootstrap = new Bootstrap();

            UDPSession session = new UDPSession(protocol, true);
            bootstrap.handler(session);
            bootstrap.group(group).channel(NioDatagramChannel.class);

            //noinspection unchecked
            options.forEach((option, value) -> bootstrap.option((ChannelOption<? super Object>) option, value));

            //noinspection unchecked
            attributeKeys.forEach((key, value) -> bootstrap.attr((AttributeKey<? super Object>) key, value));

            Channel channel = null;
            try {
                channel = host != null ? bootstrap.bind(host, port).sync().channel()
                                        : bootstrap.bind(port).sync().channel();
            } catch (InterruptedException e) {
                LOGGER.log(Level.ERROR, e);
                // Restore interrupted state...
                Thread.currentThread().interrupt();
            }

            session.setChannel(channel);
            return new HydraUDPServer(channel, group, session);
        }
    }
}
