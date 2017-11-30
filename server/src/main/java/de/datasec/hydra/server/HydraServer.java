package de.datasec.hydra.server;

import de.datasec.hydra.shared.handler.Session;
import de.datasec.hydra.shared.protocol.HydraProtocol;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.net.SocketAddress;
import java.util.Arrays;
import java.util.Set;

/**
 * Created with love by DataSec on 30.09.2017.
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

    public void close() {
        channel.close();
        Arrays.stream(loopGroups).forEach(EventExecutorGroup::shutdownGracefully);
    }

    public boolean isActive() {
        return channel.isActive();
    }

    public SocketAddress getLocalAdress() {
        return channel.localAddress();
    }

    public Set<Session> getSessions() {
        return protocol.getSessions();
    }
}