package com.marcluque.hydra.server.udp;

import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.handler.impl.UDPSession;
import com.marcluque.hydra.shared.protocol.packets.UDPPacket;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class HydraUDPServer {

    private Channel channel;

    private EventLoopGroup eventLoopGroup;

    private UDPSession udpSession;

    public HydraUDPServer(Channel channel, EventLoopGroup eventLoopGroup, UDPSession udpSession) {
        this.channel = channel;
        this.eventLoopGroup = eventLoopGroup;
        this.udpSession = udpSession;
    }

    /**
     * Closes the channel of the server, so that also the session is closed and both can't be reused.
     * This method also shuts down the so called 'event loop groups'. The event loop groups are a grouping of threads.
     * In this case there is an array of event loop groups because the server has boss and worker groups.
     */
    public void close() {
        channel.close();
        eventLoopGroup.shutdownGracefully();
    }

    /**
     * Returns whether the channel of the server is active. Basically it checks whether the pipeline is open
     * and I/O operations are possible.
     *
     * @return whether the channel of the server is active/open.
     */
    public boolean isActive() {
        return channel.isActive();
    }

    /**
     * Returns the channel (a connection/pipeline) that was created for the server. The channel allows a lot of functionality.
     * The channel provides information about the channel configuration, the channel state, the channel pipeline and much
     * more. The user is not required to use the channel for "casual" use. This method is supposed to allow in-depth work.
     *
     * @return the channel that is created for the server.
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     *
     * @return
     */
    public EventLoopGroup getEventLoopGroup() {
        return eventLoopGroup;
    }

    /**
     * Returns the local address of the server.
     * The local address is the address the socket is bound to. In this case the server is bound to a local address.
     * That is the address client connect to and refer to as remote host.
     *
     * @return the local address of the server.
     */
    public SocketAddress getLocalAdress() {
        return channel.localAddress();
    }

    /**
     *
     * @param packet
     */
    public void send(UDPPacket packet) {
        udpSession.send(packet);
    }

    /**
     *
     * @param packet
     * @param recipients
     */
    public void send(UDPPacket packet, InetSocketAddress... recipients) {
       // TODO
    }

    /**
     * Returns the set of sessions that Hydra keeps track of. This is useful when e.g. an amount of clients is connected.
     * See {@link Session} for more information what a session is.
     *
     * @return the set of sessions that the server is connected with.
     */
    public UDPSession getUdpSession() {
        return udpSession;
    }
}