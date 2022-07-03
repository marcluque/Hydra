package com.marcluque.hydra.shared.handler.impl;

import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.protocol.Protocol;
import com.marcluque.hydra.shared.protocol.packets.UDPPacket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.net.InetSocketAddress;

/*
 * Created with love by marcluque on 03.01.20
 */
public class UDPSession extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final Logger LOGGER = LogManager.getLogger(UDPSession.class.getName());

    private Channel channel;

    private final Protocol protocol;

    private final boolean isServer;

    private InetSocketAddress sender;

    public UDPSession(Protocol protocol, boolean isServer) {
        this.protocol = protocol;
        this.isServer = isServer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, DatagramPacket packet) {
        if (protocol.getPacketListener() != null) {
            channel = context.channel();
            protocol.callPacketListener(packet, this);
        } else {
            String errorMessage = "There is no packet listener registered!%n%n" +
                        "Additional information:%n" +
                        "Protocol: " + protocol + "%n" +
                        "Packet: " + packet + "%n" +
                        "Channel: " + channel;
            throw new IllegalStateException(errorMessage);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        LOGGER.log(Level.WARN, cause);

        if (!isServer) {
            context.close();
        }
    }

    public void send(UDPPacket packet) {
        channel.writeAndFlush(new DatagramPacket(packet.getByteBuf(protocol.getPacketId(packet)), packet.getRecipient()));
    }

    public <T extends Serializable> void send(T object, InetSocketAddress recipient) {
        // TODO: StandardPacket for udp
        //channel.writeAndFlush();
    }

    public <T> void send(T object, InetSocketAddress recipient) {
        // TODO
    }

    public void close() {
        channel.disconnect();
    }

    public boolean isActive() {
        return channel.isActive();
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setSender(InetSocketAddress sender) {
        this.sender = sender;
    }

    public InetSocketAddress getSender() {
        return sender;
    }

    public boolean compare(Session s) {
        return channel.id() == s.getChannel().id();
    }

    @Override
    public String toString() {
        return "UDPSession{" +
                "channel=" + channel +
                ", protocol=" + protocol +
                ", sender=" + sender +
                '}';
    }
}