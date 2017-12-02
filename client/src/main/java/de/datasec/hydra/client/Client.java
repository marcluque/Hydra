package de.datasec.hydra.client;

import de.datasec.hydra.shared.handler.listener.HydraSessionListener;
import de.datasec.hydra.shared.initializer.HydraChannelInitializer;
import de.datasec.hydra.shared.protocol.HydraProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.SocketOption;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with love by DataSec on 30.11.17
 */
public class Client {

    public static class Builder {

        private String host;

        private int port;

        private int workerThreads = 2;

        private Map<SocketOption, Object> options = new HashMap<>();

        private HydraProtocol protocol;

        public Builder(String host, int port, HydraProtocol protocol) {
            this.host = host;
            this.port = port;
            this.protocol = protocol;
        }

        public Builder workerThreads(int workerThreads) {
            this.workerThreads = workerThreads;
            return this;
        }

        public <T> Builder option(SocketOption<T> socketOption, T value) {
            options.put(socketOption, value);
            return this;
        }

        public Builder addSessionListener(HydraSessionListener sessionListener) {
            protocol.addSessionListner(sessionListener);
            return this;
        }

        public HydraClient build() {
            return setUpClient();
        }

        private HydraClient setUpClient() {
            EventLoopGroup workerGroup;
            boolean epoll = Epoll.isAvailable();

            Bootstrap bootstrap = new Bootstrap()
                    .group(workerGroup = epoll ? new EpollEventLoopGroup(workerThreads) : new NioEventLoopGroup(workerThreads))
                    .channel(epoll ? EpollSocketChannel.class : NioSocketChannel.class)
                    .remoteAddress(host, port)
                    .handler(new HydraChannelInitializer(protocol, false));

            options.forEach((option, value) -> bootstrap.option(ChannelOption.valueOf(option.name()), value));

            Channel channel = null;
            try {
                channel = bootstrap.connect().sync().channel();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return new HydraClient(channel, protocol, workerGroup);
        }
    }
}