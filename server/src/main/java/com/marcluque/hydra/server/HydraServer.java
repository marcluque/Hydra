package com.marcluque.hydra.server;

import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.protocol.Protocol;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.Future;

import java.net.SocketAddress;
import java.util.Set;

/**
 * Created with love by marcluque on 30.09.2017.
 * <br>
 * For an instruction about setup visit in the Hydra wiki the article
 * <a href="https://github.com/marcluque/Hydra/wiki/Server-setup">server setup</a>.
 * <br>
 * For an example of how to work with the server, visit
 * <a href="https://github.com/marcluque/Hydra/tree/master/server/src/test/java/server">server example</a>.
 */
public class HydraServer {

    private final Channel channel;

    private Protocol protocol;

    private final EventLoopGroup[] loopGroups;

    public HydraServer(Channel channel, Protocol protocol, EventLoopGroup[] loopGroups) {
        this.channel = channel;
        this.protocol = protocol;
        this.loopGroups = loopGroups;
    }

    /**
     * Closes the channel of the server, so that also the session is closed and both can't be reused.
     * This method also shuts down the so called 'event loop groups'. The event loop groups are a grouping of threads.
     * In this case there is an array of event loop groups because the server has boss and worker groups.
     */
    public Future<?>[] close() {
        protocol.getSessions().forEach(Session::close);
        protocol.getSessions().forEach(s -> s = null);
        protocol = null;
        Future<?>[] futures = new Future<?>[loopGroups.length];
        for (int i = 0; i < loopGroups.length; i++) {
            futures[i] = loopGroups[i].shutdownGracefully();
        }

        return futures;
    }

    /**
     * Returns whether the channel of the server is active. Basically that means if the connection/pipeline is open
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
     * Returns the boss group accepts incoming connections. The boss group handles all incoming connection requests.
     * Once the boss group accepted a connection, the traffic is handled by the worker group.
     *
     * @return the boss group that handles incoming connections.
     */
    public EventLoopGroup getBossGroup() {
        return loopGroups[0];
    }

    /**
     * Returns the worker group that handles I/O operations and allows to register channels. The worker group handles
     * traffic of created connections, once the boss group accepted a specific connection.
     *
     * @return the worker group that handles the I/O operations (traffic).
     */
    public EventLoopGroup getWorkerGroup() {
        return loopGroups[1];
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
     * Returns the set of sessions that Hydra keeps track of. This is useful when e.g. an amount of clients is connected.
     * See {@link Session} for more information what a session is.
     *
     * @return the set of sessions that the server is connected with.
     */
    public Set<Session> getSessions() {
        return protocol.getSessions();
    }
}