package de.datasecs.hydra.server;

import de.datasecs.hydra.shared.handler.HydraSession;
import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.protocol.HydraProtocol;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.net.SocketAddress;
import java.util.Arrays;
import java.util.Set;

/**
 * Created with love by DataSecs on 30.09.2017.
 * <br>
 * For an instruction about setup visit in the Hydra wiki the article
 * <a href="https://github.com/DataSecs/Hydra/wiki/Server-setup">server setup</a>.
 * <br>
 * For an example of how to work with the server, visit
 * <a href="https://github.com/DataSecs/Hydra/tree/master/server/src/test/java/server">server example</a>.
 */
public class HydraServer {

    private Channel channel;

    private HydraProtocol protocol;

    private EventLoopGroup[] loopGroups;

    public HydraServer(Channel channel, HydraProtocol protocol, EventLoopGroup[] loopGroups) {
        this.channel = channel;
        this.protocol = protocol;
        this.loopGroups = loopGroups;
    }

    /**
     * Closes the channel of the server, so that also the session is closed and both can't be reused.
     * This method also shuts down the so called 'event loop groups'. The event loop groups are a grouping of threads.
     * In this case there is an array of event loop groups because the server has boss and worker groups.
     */
    public void close() {
        channel.close();
        Arrays.stream(loopGroups).forEach(EventExecutorGroup::shutdownGracefully);
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
     * more. The user is not required to use the channel normal use. This method is supposed to allow in-depth work.
     *
     * @return the channel that is created for the server.
     */
    public Channel getChannel() {
        return channel;
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
     * Returns the set of sessions that Hydra keeps track of. This is useful when e.g. an amount of clients is connected.
     * See {@link HydraSession} for more information what a session is.
     *
     * @return the set of sessions that the server is connected with.
     */
    public Set<Session> getSessions() {
        return protocol.getSessions();
    }
}