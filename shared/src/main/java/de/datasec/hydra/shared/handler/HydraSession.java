package de.datasec.hydra.shared.handler;

import de.datasec.hydra.shared.protocol.HydraProtocol;
import de.datasec.hydra.shared.protocol.packets.Packet;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.SocketAddress;

/**
 * Created by DataSec on 29.09.2017.
 */
public class HydraSession extends SimpleChannelInboundHandler<Packet> {

    private Channel channel;

    private HydraProtocol protocol;

    public HydraSession(Channel channel, HydraProtocol protocol) {
        this.channel = channel;
        this.protocol = protocol;
        protocol.setSession(this);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, Packet packet) throws Exception {
        protocol.callListener(packet);
    }

    public void send(Packet packet) {
        channel.writeAndFlush(packet);
    }

    public void close() {
        channel.close();
    }

    public boolean isConnected() {
        return channel.isActive();
    }

    public HydraProtocol getProtocol() {
        return protocol;
    }

    public SocketAddress getRemoteAdress() {
        return channel.remoteAddress();
    }
}
