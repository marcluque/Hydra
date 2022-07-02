package com.marcluque.hydra.client.udp;

import com.marcluque.hydra.shared.handler.impl.UDPSession;
import com.marcluque.hydra.shared.protocol.packets.UDPPacket;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;

import java.io.Serializable;
import java.net.SocketAddress;

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

    /**
     * @return the channel that is created for the server.
     */
    @Override
    public Channel channel() {
        return channel;
    }

    /**
     * Returns the worker group that handles I/O operations and allows to register channels. The worker group handles
     * traffic.
     *
     * @return the worker group that handles the I/O operations (traffic).
     */
    @Override
    public EventLoopGroup workerGroup() {
        return workerGroup;
    }
}