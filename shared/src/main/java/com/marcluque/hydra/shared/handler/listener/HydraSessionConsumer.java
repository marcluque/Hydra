package com.marcluque.hydra.shared.handler.listener;

import com.marcluque.hydra.shared.handler.Session;

import java.util.function.Consumer;

/**
 * Created with love by marcluque on 08.06.18
 */
public class HydraSessionConsumer {

    private Consumer<Session> onConnectedConsumer;

    private Consumer<Session> onDisconnectedConsumer;

    public Consumer<Session> getOnConnectedConsumer() {
        return onConnectedConsumer;
    }

    public void setOnConnectedConsumer(Consumer<Session> onConnectedConsumer) {
        this.onConnectedConsumer = onConnectedConsumer;
    }

    public Consumer<Session> getOnDisconnectedConsumer() {
        return onDisconnectedConsumer;
    }

    public void setOnDisconnectedConsumer(Consumer<Session> onDisconnectedConsumer) {
        this.onDisconnectedConsumer = onDisconnectedConsumer;
    }
}