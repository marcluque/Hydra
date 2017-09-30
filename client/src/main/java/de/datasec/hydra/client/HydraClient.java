package de.datasec.hydra.client;

import de.datasec.hydra.shared.handler.HydraSession;
import de.datasec.hydra.shared.initializer.HydraChannelInitializer;
import de.datasec.hydra.shared.protocol.HydraProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.SocketOption;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DataSec on 29.09.2017.
 */
public class HydraClient {

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

        public HydraSession build() {
            return setUpClient();
        }

        private HydraSession setUpClient() {
            HydraChannelInitializer initializer = new HydraChannelInitializer(protocol);

            try {
                Bootstrap bootstrap = new Bootstrap()
                        .group(new NioEventLoopGroup(workerThreads))
                        .channel(NioSocketChannel.class)
                        .remoteAddress(host, port)
                        .handler(initializer);

                // TODO: THINK OF DIFFERENT SOLUTION FOR CHANNELOPTION
                options.forEach((option, value) -> bootstrap.option(ChannelOption.valueOf(option.name()), value));

                bootstrap.connect().sync();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return initializer.getSession();
        }
    }
}
