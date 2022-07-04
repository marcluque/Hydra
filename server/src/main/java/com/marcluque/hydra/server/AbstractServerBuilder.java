package com.marcluque.hydra.server;

import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.handler.listener.HydraSessionListener;
import com.marcluque.hydra.shared.protocol.Protocol;
import io.netty.channel.ChannelOption;
import io.netty.util.AttributeKey;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractServerBuilder<T> {

    protected String host;

    protected int port;

    protected int workerThreads = 2;

    protected int bossThreads = 1;

    protected final Map<ChannelOption<?>, Object> options = new HashMap<>();

    protected final Map<ChannelOption<?>, Object> childOptions = new HashMap<>();

    protected final Map<AttributeKey<?>, Object> attributeKeys = new HashMap<>();

    protected final Map<AttributeKey<?>, Object> childAttributeKeys = new HashMap<>();

    protected boolean useEpoll;

    protected Protocol protocol;

    protected AbstractServerBuilder(int port, Protocol protocol) {
        this.port = port;
        this.protocol = protocol;
    }

    /**
     * If the socket has no wildcard address it can't receive broadcast packets.
     * You may set {@link ChannelOption} SO_BROADCAST to false and then use a host address.
     *
     * @param host host the server will be bound to.
     */
    public AbstractServerBuilder<T> host(String host) {
        this.host = host;
        return this;
    }

    /**
     * Sets the number of worker threads for the server.
     * A worker thread performs non-blocking operations for one or more channels.
     * The standard amount is set to 2.
     *
     * @param workerThreads the amount of worker threads for the server
     */
    public AbstractServerBuilder<T> workerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
        return this;
    }

    /**
     * Sets the number of boss threads for the server.
     * The standard number is set to 1.
     * Netty sets the standard amount to twice the amount of processors/cores.
     *
     * @param bossThreads the amount of boss threads for the server
     */
    public AbstractServerBuilder<T> bossThreads(int bossThreads) {
        this.bossThreads = bossThreads;
        return this;
    }

    /**
     * Adds a specific option to the server that is added to the channel configuration.
     * These options include a lot of possibilities.
     *
     * @param channelOption the desired channel option
     * @param value the value that is supposed to be set for the desired channel option
     *
     * @see <a href="https://netty.io/4.1/api/io/netty/channel/ChannelOption.html">channel options</a>
     */
    public <S> AbstractServerBuilder<T> option(ChannelOption<S> channelOption, S value) {
        options.put(channelOption, value);
        return this;
    }

    /**
     * Adds a specific option to the connections that are opened with the server's channel.
     *
     * @param channelOption the desired channel option
     * @param value the value that is supposed to be set for the desired channel option
     */
    public <S> AbstractServerBuilder<T> childOption(ChannelOption<S> channelOption, S value) {
        childOptions.put(channelOption, value);
        return this;
    }

    /**
     * Adds a specific attribute to the server. The attributes are saved in an attribute map by Netty.
     *
     * @param attributeKey the attribute key that is supposed to be stored in the map.
     * @param value the value that is supposed to be mapped to the given attribute key.
     */
    public <S> AbstractServerBuilder<T> attribute(AttributeKey<S> attributeKey, S value) {
        attributeKeys.put(attributeKey, value);
        return this;
    }

    /**
     * Adds a specific child attribute to the server. Child attributes apply to the connections that the server creates,
     * just like child options. The attributes are saved in an attribute map by Netty.
     *
     * @param attributeKey the attribute key that is supposed to be stored in the map.
     * @param value the value that is supposed to be mapped to the given attribute key.
     */
    public <S> AbstractServerBuilder<T> childAttribute(AttributeKey<S> attributeKey, S value) {
        childAttributeKeys.put(attributeKey, value);
        return this;
    }

    /**
     * Epoll decides whether it's a unix based system netty is operating on, or not.
     * This method gives the possibility to allow the usage of epoll, if it's available.
     *
     * @param useEpoll sets whether epoll should be used or not
     */
    public AbstractServerBuilder<T> useEpoll(boolean useEpoll) {
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
    public AbstractServerBuilder<T> addListener(HydraSessionListener sessionListener) {
        protocol.addSessionListener(sessionListener);
        return this;
    }

    public AbstractServerBuilder<T> onConnected(Consumer<Session> onConnectedConsumer) {
        protocol.getSessionConsumer().setOnConnectedConsumer(onConnectedConsumer);
        return this;
    }

    public AbstractServerBuilder<T> onDisconnected(Consumer<Session> onDisconnectedConsumer) {
        protocol.getSessionConsumer().setOnDisconnectedConsumer(onDisconnectedConsumer);
        return this;
    }

    public abstract T build();
}
