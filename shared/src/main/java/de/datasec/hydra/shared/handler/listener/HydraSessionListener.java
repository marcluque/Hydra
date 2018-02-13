package de.datasec.hydra.shared.handler.listener;

import de.datasec.hydra.shared.handler.Session;

/**
 *
 */
public interface HydraSessionListener {

    void onConnected(Session session);

    void onDisconnected(Session session);
}
