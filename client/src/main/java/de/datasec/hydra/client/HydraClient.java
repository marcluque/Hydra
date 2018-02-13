package de.datasec.hydra.client;

import de.datasec.hydra.shared.handler.Session;
import de.datasec.hydra.shared.protocol.HydraProtocol;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;

import java.net.SocketAddress;

/**
 * Created with love by DataSec on 29.09.2017.
 */
public class HydraClient {

    private Channel channel;

    private HydraProtocol protocol;

    private EventLoopGroup workerGroup;

    public HydraClient(Channel channel, HydraProtocol protocol, EventLoopGroup workerGroup) {
        this.channel = channel;
        this.protocol = protocol;
        this.workerGroup = workerGroup;
    }

    /**
     * Closes the channel of the client, so that the session is closed and can't be reused.
     * This method also shuts down the workerGroup, as it's not needed anymore, when the session is closed.
     */
    public void close() {
        channel.close();
        workerGroup.shutdownGracefully();
    }

    /**
     * Returns whether the channel is connected and writable.
     *
     * @return whether channel is connected.
     */
    public boolean isConnected() {
        return channel.isWritable();
    }

    /**
     * Returns the channel (a connection/pipeline) that was created for the server. The channel allows a lot of functionality.
     * The channel provides information about the channel configuration, the channel state, the channel pipeline and much
     * more. The user is not required to use the channel normal use. This method is supposed to allow in-depth work.
     *
     * @return the channel that is created for the server.
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * Returns the remote address the client is connected to. The client is not bound to a local address.
     * It just has a remote address that it's connected to.
     *
     * @return the remote address the client is connected to.
     */
    public SocketAddress getRemoteAddress() {
        return channel.remoteAddress();
    }

    /**
     * Returns the session that is created, when the client connects with the server.
     * {@link de.datasec.hydra.shared.handler.HydraSession} for more information about what a session is.
     *
     * @return the session created for client and server.
     */
    public Session getSession() {
        return protocol.getClientSession();
    }
}