package de.datasecs.hydra.shared.handler.listener;

import de.datasecs.hydra.shared.handler.Session;

/**
 * Created with love by DataSecs on 02.12.2017.
 *
 * The session listener interface is supposed to provide the user the opportunity to easily catch the probably
 * most important session events. The connecting and disconnecting of clients. The interface works for both sides,
 * client, as well as server.
 */
public interface HydraSessionListener {

    /**
     * This method gets triggered when the client is successfully connected to the aimed server.
     *
     * @param session the session that is supposed to be listened on.
     */
    void onConnected(Session session);

    /**
     * This method gets fired when the client is disconnected from the server it was connected to.
     *
     * @param session the session that is supposed to be listened on.
     */
    void onDisconnected(Session session);
}
