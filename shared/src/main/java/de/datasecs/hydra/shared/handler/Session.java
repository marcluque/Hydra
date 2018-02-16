package de.datasecs.hydra.shared.handler;

import de.datasecs.hydra.shared.protocol.packets.Packet;

import java.net.SocketAddress;

/**
 * Created with love by DataSecs on 01.10.2017.
 *
 * A session in Hydra represents a connection between two sockets. It includes netty's channel and all other options to
 * write to the pipeline. The session is supposed to be the most important part for the user, as it provides the possibility
 * to write to the channel of the session and interact with the connected opponent (client or server,
 * depending of where the session is accessed).
 */
public interface Session {

    /**
     * Sends a packet to the opponent that is connected with this session.
     *
     * @param packet the packet that is supposed to be send to the opponent of the session.
     */
    void send(Packet packet);

    /**
     * Closes the session of the server and client and therefore disconnects from the channel
     */
    void close();

    /**
     * Returns whether the calling opponent is connected the other one. More precise this method checks whether
     * the channel is active.
     *
     * @return whether the session is active.
     */
    boolean isConnected();

    /**
     * Returns depending of the calling opponent the remote or local address. In case of the server calling this method
     * the local address is returned. In case of the client calling the remote address is returned.
     * The local address of the server is the remote address of the client.
     *
     * @return the remote or local address, depending of calling opponent.
     */
    SocketAddress getAddress();
}