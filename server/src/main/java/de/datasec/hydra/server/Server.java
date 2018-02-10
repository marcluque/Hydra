package de.datasec.hydra.server;

import de.datasec.hydra.shared.handler.listener.HydraSessionListener;
import de.datasec.hydra.shared.initializer.HydraChannelInitializer;
import de.datasec.hydra.shared.protocol.HydraProtocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with love by DataSec on 29.11.17
 */
public class Server {

    public static class Builder {

        private String host;

        private int port;

        private int workerThreads = 2;

        private int bossThreads = 1;

        private Map<ChannelOption, Object> options = new HashMap<>();

        private Map<ChannelOption, Object> childOptions = new HashMap<>();

        private boolean useEpoll;

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

        public Builder bossThreads(int bossThreads) {
            this.bossThreads = bossThreads;
            return this;
        }

        public <T> Builder option(ChannelOption<T> channelOption, T value) {
            options.put(channelOption, value);
            return this;
        }

        public <T> Builder childOption(ChannelOption<T> channelOption, T value) {
            childOptions.put(channelOption, value);
            return this;
        }

        public Builder useEpoll(boolean useEpoll) {
            this.useEpoll = useEpoll;
            return this;
        }

        public Builder addListener(HydraSessionListener sessionListener) {
            protocol.addSessionListener(sessionListener);
            return this;
        }

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