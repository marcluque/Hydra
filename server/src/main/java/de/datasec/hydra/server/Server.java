package de.datasec.hydra.server;

import de.datasec.hydra.shared.initializer.HydraChannelInitializer;
import de.datasec.hydra.shared.protocol.HydraProtocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.SocketOption;
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

        private NioEventLoopGroup workerGroup;

        private NioEventLoopGroup bossGroup;

        private Map<SocketOption, Object> options = new HashMap<>();

        private Map<SocketOption, Object> childOptions = new HashMap<>();

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

        public <T> Builder option(SocketOption<T> socketOption, T value) {
            options.put(socketOption, value);
            return this;
        }

        public <T> Builder childOption(SocketOption<T> socketOption, T value) {
            childOptions.put(socketOption, value);
            return this;
        }

        public HydraServer build() {
            return setUpServer();
        }

        private HydraServer setUpServer() {
            Channel channel = null;

            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HydraChannelInitializer(protocol));

            options.forEach((option, value) -> serverBootstrap.option(ChannelOption.valueOf(option.name()), value));
            childOptions.forEach((option, value) -> serverBootstrap.childOption(ChannelOption.valueOf(option.name()), value));

            try {
                channel = serverBootstrap.bind(host, port).sync().channel();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Needs to be created here already and overridden in the channel initializer,
            // as the channel is not immediately initialized
            return new HydraServer(channel, protocol, new NioEventLoopGroup[]{bossGroup = new NioEventLoopGroup(bossThreads), workerGroup = new NioEventLoopGroup(workerThreads)});
        }
    }
}