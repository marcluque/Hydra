package com.marcluque.hydra.shared.handler.listener;

import com.marcluque.hydra.shared.handler.Session;

/**
 * Created with love by marcluque on 02.12.2017.
 *
 * The session listener interface is supposed to provide the user the opportunity to easily catch the probably
 * most important session events. The connecting and disconnecting of clients. The interface works for both sides,
 * client, and server.
 * <br>
 * For an instruction about usage of the session listener visit the article
 * <a href="https://github.com/marcluque/Hydra/wiki/Sessions">sessions</a> in the Hydra wiki.
 * <br>
 * For an example of how to work with the session listener, visit the
 * <a href="https://github.com/marcluque/Hydra/tree/master/client/src/test/java/client">client example</a>
 * or the <a href="https://github.com/marcluque/Hydra/tree/master/server/src/test/java/server">server example</a>.
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
