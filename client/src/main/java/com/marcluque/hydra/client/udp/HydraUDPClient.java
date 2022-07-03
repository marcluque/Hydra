package com.marcluque.hydra.client.udp;

import com.marcluque.hydra.shared.handler.impl.UDPSession;
import com.marcluque.hydra.shared.protocol.packets.UDPPacket;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;

import java.io.Serializable;
import java.net.SocketAddress;

/**
 *
 * @param channel Returns the channel (a connection/pipeline) that was created for the client.
 *                The channel allows a lot of functionality.
 *                It provides information about the channel configuration,
 *                the channel state, the channel pipeline and much more.
 *                The user is not required to work with the channel.
 *                This method is supposed to allow in-depth work.
 * @param workerGroup TODO
 * @param udpSession Returns the set of sessions that Hydra keeps track of.
 *                   This is useful when e.g. an amount of clients is connected.
 *                   See {@link UDPSession} for more information what a session is.
 */
public record HydraUDPClient(Channel channel, EventLoopGroup workerGroup, UDPSession udpSession) {

    /**
     * Closes the channel of the client, so that the session is closed and can't be reused.
     * This method also shuts down the workerGroup, as it's not needed anymore, when the session is closed.
     */
    public void close() {
        channel.close();
        workerGroup.shutdownGracefully();
    }

    /**
     * Returns whether the channel is active.
     *
     * @return whether channel is active.
     */
    public boolean isActive() {
        return channel.isActive();
    }

    /**
     * Returns whether the channel is writable.
     *
     * @return whether channel is writable.
     */
    public boolean isWriteable() {
        return channel.isWritable();
    }

    /**
     * Returns the local address of the server.
     * The local address is the address the socket is bound to. In this case the server is bound to a local address.
     * That is the address client connect to and refer to as remote host.
     *
     * @return the local address of the server.
     */
    public SocketAddress getLocalAddress() {
        return channel.localAddress();
    }

    /**
     * Sends a packet to the recipient that is specified in the packet.
     *
     * @param packet the packet that is supposed to be sent to the recipient.
     */
    public void send(UDPPacket packet) {
        udpSession.send(packet);
    }

    /**
     * @param object the object that is supposed to be sent to the opponent of the session.
     */
    public <T extends Serializable> void send(T object) {
        // TODO
    }
}