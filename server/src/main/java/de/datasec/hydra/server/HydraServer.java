package de.datasec.hydra.server;

import de.datasec.hydra.shared.handler.HydraSession;
import de.datasec.hydra.shared.initializer.HydraChannelInitializer;
import de.datasec.hydra.shared.protocol.HydraProtocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.SocketOption;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DataSec on 30.09.2017.
 */
public class HydraServer {

    public static class Builder {

        private String host;

        private int port;

        private int workerThreads = 2;

        private int bossThreads = 1;

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

        public HydraSession build() {
            return setUpClient();
        }

        private HydraSession setUpClient() {
            HydraChannelInitializer initializer = new HydraChannelInitializer(protocol);

            try {
                ServerBootstrap serverBootstrap = new ServerBootstrap()
                        .group(new NioEventLoopGroup(bossThreads), new NioEventLoopGroup(workerThreads))
                        .channel(NioServerSocketChannel.class)
                        .childHandler(initializer);

                // TODO: SEE CLIENT TODO
                options.forEach((option, value) -> serverBootstrap.option(ChannelOption.valueOf(option.name()), value));
                childOptions.forEach((option, value) -> serverBootstrap.childOption(ChannelOption.valueOf(option.name()), value));

                serverBootstrap.bind(host, port).sync().channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return initializer.getSession();
        }
    }
}
