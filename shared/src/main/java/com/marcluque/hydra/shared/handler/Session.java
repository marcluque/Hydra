package com.marcluque.hydra.shared.handler;

import com.marcluque.hydra.shared.protocol.packets.Packet;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.io.Serializable;
import java.net.SocketAddress;

/**
 * Created with love by marcluque on 01.10.2017.
 *
 * A session in Hydra represents a connection between two sockets. It includes netty's channel and all other options to
 * write to the pipeline. The session is supposed to be the most important part for the user, as it provides the possibility
 * to write to the channel of the session and interact with the connected opponent (client or server,
 * depending on where the session is accessed).
 * <br>
 * For an instruction about usage visit the article
 * <a href="https://github.com/marcluque/Hydra/wiki/Sessions">sessions</a> in the Hydra wiki.
 * <br>
 * For an example of how to work with the session, visit the
 * <a href="https://github.com/marcluque/Hydra/tree/master/client/src/test/java/client">client example</a>
 * or the <a href="https://github.com/marcluque/Hydra/tree/master/server/src/test/java/server">server example</a>.
 */
public interface Session {

    /**
     * Sends a packet to the opponent that is connected with this session.
     *
     * @param packet the packet that is supposed to be sent to the opponent of the session.
     */
    <T extends Packet> ChannelFuture send(T packet);

    /**
     * Sends a packet to the opponent that is connected with this session. With the difference that the param not is a
     * packet. The packet is created internally and then send to the opponent, so the user doesn't have to bother with
     * the packet creation. Therefore, the object that is passed to the method has to be serializable.
     * See {@link com.marcluque.hydra.shared.protocol.packets.StandardPacket} for the structure of the standard packet.
     *
     * @param object the object that is supposed to be sent to the opponent of the session.
     */
    <T extends Serializable> ChannelFuture send(T object);

    /**
     * Closes the session of the server and client and therefore disconnects from the channel
     */
    ChannelFuture close();

    /**
     * Returns whether the calling opponent is connected the other one. More precise this method checks whether
     * the channel is active.
     *
     * @return whether the session is active.
     */
    boolean isConnected();

    /**
     * Returns the channel (a connection/pipeline) that was created for the server. The channel allows a lot of functionality.
     * The channel provides information about the channel configuration, the channel state, the channel pipeline and much
     * more. The user is not required to use the channel for "casual" use. This method is supposed to allow in-depth work.
     *
     * @return the channel that is created for the server.
     */
    Channel getChannel();

    /**
     * Returns depending on the calling opponent the remote or local address. In case of the server calling this method
     * the local address is returned. In case of the client calling the remote address is returned.
     * The local address of the server is the remote address of the client.
     *
     * @return the remote or local address, depending on calling opponent.
     */
    SocketAddress getAddress();

    /**
     * Returns true if sessions are equal. Comparison happens based on netty channel id.
     *
     * @param s a session to compare to this session
     * @return true, if passed session equals this session
     */
    boolean compare(Session s);
}