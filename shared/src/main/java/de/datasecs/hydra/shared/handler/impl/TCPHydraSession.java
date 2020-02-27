package de.datasecs.hydra.shared.handler.impl;

import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.protocol.Protocol;
import de.datasecs.hydra.shared.protocol.packets.Packet;
import de.datasecs.hydra.shared.protocol.packets.StandardPacket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.Serializable;
import java.net.SocketAddress;
import java.util.Objects;

/**
 * Created with love by DataSecs on 29.09.2017.
 */
public class TCPHydraSession extends SimpleChannelInboundHandler<Packet> implements Session {

    private Channel channel;

    private Protocol protocol;

    public TCPHydraSession(Channel channel, Protocol protocol) {
        this.channel = channel;
        this.protocol = protocol;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, Packet packet) {
        if (protocol.getPacketListener() != null) {
            protocol.callPacketListener(packet, this);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext context) {
        if (protocol.getSessionListener() != null) {
            protocol.callSessionListener(false, this);
        } else if (protocol.getSessionConsumer() != null) {
            protocol.callSessionConsumer(false, this);
        }

        protocol.removeSession(this);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }

    @Override
    public void send(Packet packet) {
        channel.writeAndFlush(packet);
    }

    @Override
    public <T extends Serializable> void send(T object) {
        channel.writeAndFlush(new StandardPacket(object));
    }

    @Override
    public void close() {
        channel.disconnect();
    }

    @Override
    public boolean isConnected() {
        return channel.isActive();
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public SocketAddress getAddress() {
        return channel.remoteAddress() == null ? channel.localAddress() : channel.remoteAddress();
    }

    @Override
    public boolean compare(Session s) {
        return channel.id() == s.getChannel().id();
    }

    @Override
    public int hashCode() {
        return channel.id().hashCode();
    }

    @Override
    public String toString() {
        return "TCPHydraSession{" +
                "channel=" + channel +
                ", protocol=" + protocol +
                ", connected=" + channel.isActive() +
                ", writable=" + channel.isWritable() +
                ", address=" + getAddress() +
                '}';
    }
}