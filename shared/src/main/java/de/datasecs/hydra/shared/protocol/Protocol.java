package de.datasecs.hydra.shared.protocol;

import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.handler.listener.HydraSessionListener;
import de.datasecs.hydra.shared.protocol.packets.Packet;
import de.datasecs.hydra.shared.protocol.packets.listener.HydraPacketListener;

import java.util.Set;

/**
 * Created with love by DataSecs on 03.06.18
 */
public interface Protocol {

    Packet createPacket(byte id);

    byte getPacketId(Packet packet);

    /**
     * Used to register a packet to the protocol of Hydra.
     * The protocol needs the information about the packet in order to be able to rebuild it after serialization.
     *
     * @param clazz the class of the Packet that is supposed to be registered.
     */
    void registerPacket(Class<? extends Packet> clazz);

    /**
     * Register listener in the protocol in order to make Hydra able to call
     * the listener when a fitting packet is received.
     *
     * @param packetListener the packet listener that is supposed to be registered.
     */
    void registerListener(HydraPacketListener packetListener);

    void callPacketListener(Packet packet, Session session);

    void addSessionListener(HydraSessionListener sessionListener);

    void callSessionListener(boolean connected, Session session);

    void setClientSession(Session clientSession);

    Session getClientSession();

    void addSession(Session session);

    void removeSession(Session session);

    Set<Session> getSessions();

    HydraSessionListener getSessionListener();

    HydraPacketListener getPacketListener();
}