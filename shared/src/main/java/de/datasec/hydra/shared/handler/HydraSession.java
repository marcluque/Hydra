package de.datasec.hydra.shared.handler;

import de.datasec.hydra.shared.protocol.HydraProtocol;
import de.datasec.hydra.shared.protocol.packets.Packet;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;

import java.net.SocketAddress;

/**
 * Created by DataSec on 29.09.2017.
 */
public class HydraSession extends SimpleChannelInboundHandler<Packet> implements Session {

    private Channel channel;

    private NioEventLoopGroup[] loopGroups;

    private HydraProtocol protocol;

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
        channel.writeAndFlush(packet);
    }

    @Override
    public void close() {
        channel.disconnect();

        for (NioEventLoopGroup group : loopGroups) {
            group.shutdownGracefully();
        }
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
    public SocketAddress getRemoteAdress() {
        return channel.remoteAddress();
    }
}
