package de.datasec.hydra.client;

import de.datasec.hydra.shared.handler.Session;
import de.datasec.hydra.shared.protocol.HydraProtocol;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;

import java.net.SocketAddress;

/**
 * Created with love by DataSec on 29.09.2017.
 */
public class HydraClient {

    private Channel channel;

    private HydraProtocol protocol;

    private NioEventLoopGroup workerGroup;

    public HydraClient(Channel channel, HydraProtocol protocol, NioEventLoopGroup workerGroup) {
        this.channel = channel;
        this.protocol = protocol;
        this.workerGroup = workerGroup;
    }

    public void close() {
        channel.close();
        workerGroup.shutdownGracefully();
    }

    public boolean isConnected() {
        return channel.isWritable();
    }

    public SocketAddress getRemoteAddress() {
        return channel.remoteAddress();
    }

    public Session getSession() {
        return protocol.getClientSession();
    }
}