package com.marcluque.hydra.shared.handler.impl;

import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.protocol.Protocol;
import com.marcluque.hydra.shared.protocol.packets.Packet;
import com.marcluque.hydra.shared.protocol.packets.StandardPacket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.net.SocketAddress;


/**
 * Created with love by marcluque on 29.09.2017.
 */
public class HydraSession extends SimpleChannelInboundHandler<Packet> implements Session {

    private static final Logger LOGGER = LogManager.getLogger(HydraSession.class.getName());

    private final Channel channel;

    private Protocol protocol;

    public HydraSession(Channel channel, Protocol protocol) {
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

        protocol = null;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.log(Level.WARN, cause);
    }

    @Override
    public ChannelFuture send(Packet packet) {
        return channel.writeAndFlush(packet);
    }

    @Override
    public <T extends Serializable> ChannelFuture send(T object) {
        return channel.writeAndFlush(new StandardPacket(object));
    }

    @Override
    public ChannelFuture close() {
        return channel.disconnect();
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
    public String toString() {
        return "HydraSession{" +
                "channel=" + channel +
                ", protocol=" + protocol +
                ", connected=" + channel.isActive() +
                ", writable=" + channel.isWritable() +
                ", address=" + getAddress() +
                '}';
    }
}