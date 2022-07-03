package com.marcluque.hydra.client;

import com.marcluque.hydra.client.tcp.HydraTCPClient;
import com.marcluque.hydra.shared.handler.listener.HydraSessionListener;
import com.marcluque.hydra.shared.protocol.Protocol;
import io.netty.channel.ChannelOption;
import io.netty.util.AttributeKey;

import java.util.HashMap;
import java.util.Map;

public abstract class HydraAbstractClientBuilder<T> {

    protected String host;

    protected int port;

    protected int workerThreads = 2;

    protected final Map<ChannelOption<?>, Object> options = new HashMap<>();

    protected final Map<AttributeKey<?>, Object> attributeKeys = new HashMap<>();

    protected boolean connectAfterSetup = true;

    protected boolean useEpoll;

    protected Protocol protocol;

    protected HydraAbstractClientBuilder(int port, Protocol protocol) {
        this.port = port;
        this.protocol = protocol;
    }

    /**
     * If the socket has no wildcard address it can't receive broadcast packets.
     * You may set {@link ChannelOption} SO_BROADCAST to false and then use a host address.
     *
     * @param host host the server will be bound to.
     */
    public HydraAbstractClientBuilder<T> host(String host) {
        this.host = host;
        return this;
    }

    /**
     * Sets the number of worker threads for the client.
     * A worker thread performs non-blocking operations for one or more channels.
     * The standard amount is set to 2.
     *
     * @param workerThreads the amount of worker threads for the client
     */
    public HydraAbstractClientBuilder<T> workerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
        return this;
    }

    /**
     * Adds a specific option to the client. These options include a lot of possibilities.
     *
     * @param channelOption the desired channel option
     * @param value the value that is supposed to be set for the desired channel option
     *
     * @see <a href="https://netty.io/4.1/api/io/netty/channel/ChannelOption.html">Channel options</a>
     */
    public <S> HydraAbstractClientBuilder<T> option(ChannelOption<S> channelOption, S value) {
        options.put(channelOption, value);
        return this;
    }

    /**
     * Adds a specific attribute to the client. The attributes are saved in an attribute map by Netty.
     *
     * @param attributeKey the attribute key that is supposed to be stored in the map.
     * @param value the value that is supposed to be mapped to the given attribute key.
     */
    public <S> HydraAbstractClientBuilder<T> attribute(AttributeKey<S> attributeKey, S value) {
        attributeKeys.put(attributeKey, value);
        return this;
    }

    /**
     * This attribute determines whether the client connects instantly or just when the 'connect()' method
     * from {@link HydraTCPClient} is called. The standard value is true, so the standard setting makes the client
     * connect instantly after setup.
     *
     * @param connectAfterSetup determines whether the client connects instantly or just when 'connect()' method is called
     */
    public HydraAbstractClientBuilder<T> connectAfterSetup(boolean connectAfterSetup) {
        this.connectAfterSetup = connectAfterSetup;
        return this;
    }

    /**
     * Basically epoll decides whether it's a unix based system netty is operating on, or not.
     * This method gives the possibility to allow the usage of epoll, if it's available.
     *
     * @param useEpoll sets whether epoll should be used or not
     */
    public HydraAbstractClientBuilder<T> useEpoll(boolean useEpoll) {
        this.useEpoll = useEpoll;
        return this;
    }

    /**
     * This method adds a session listener to the client. The session listener has 2 methods.
     * The first one is 'onConnected', which gets triggered when the client is successfully connected to the aimed server.
     * The second on is 'onDisconnected', which gets fired when the client is disconnected from the server it was
     * connected to.
     *
     * @param sessionListener this method takes an instance of the session listener interface HydraSessionListener.
     */
    public HydraAbstractClientBuilder<T> addSessionListener(HydraSessionListener sessionListener) {
        protocol.addSessionListener(sessionListener);
        return this;
    }

    public abstract T build();
}
