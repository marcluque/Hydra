package de.datasec.hydra.shared.handler;

import de.datasec.hydra.shared.protocol.HydraProtocol;
import de.datasec.hydra.shared.protocol.packets.Packet;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.AbstractEventExecutorGroup;

import java.net.SocketAddress;
import java.util.Arrays;

/**
 * Created by DataSec on 29.09.2017.
 */
public class HydraSession extends SimpleChannelInboundHandler<Packet> implements Session {

    private Channel channel;

    private HydraProtocol protocol;

    private NioEventLoopGroup[] loopGroups;

    public HydraSession(Channel channel, HydraProtocol protocol, NioEventLoopGroup[] loopGroups) {
        this.channel = channel;
        this.protocol = protocol;
        protocol.setSession(this);
        this.loopGroups = loopGroups;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, Packet packet) throws Exception {
        protocol.callListener(packet);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void send(Packet packet) {
        // TODO: Implement custom class serialization
        channel.writeAndFlush(packet);
    }

    @Override
    public void close() {
        channel.disconnect();
        Arrays.stream(loopGroups).forEach(AbstractEventExecutorGroup::shutdownGracefully);
    }

    @Override
    public boolean isConnected() {
        return channel.isActive();
    }

    @Override
    public HydraProtocol getProtocol() {
        return protocol;
    }

    @Override
    public SocketAddress getRemoteAddress() {
        SocketAddress address = channel.remoteAddress();
        return address == null ? channel.localAddress() : address;
    }
}