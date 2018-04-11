package de.datasecs.hydra.example.server.chat;

import de.datasecs.hydra.example.shared.chat.MessagePacket;
import de.datasecs.hydra.example.shared.chat.ServerPacket;
import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.protocol.packets.listener.HydraPacketListener;
import de.datasecs.hydra.shared.protocol.packets.listener.PacketHandler;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with love by DataSecs on 11.04.18
 */
public class ChatServerPacketListener implements HydraPacketListener {

    private Set<Session> sessions;

    public ChatServerPacketListener(Set<Session> sessions) {
        this.sessions = sessions;
    }

    @PacketHandler
    public void onMessagePacket(MessagePacket messagePacket, Session session) {
        Set<Session> sessionsCopy = new HashSet<>(sessions);
        sessionsCopy.remove(session);
        sessionsCopy.forEach(receiverSession -> receiverSession.send(messagePacket));
    }

    @PacketHandler
    public void onServerPacket(ServerPacket serverPacket, Session session) {
        Set<Session> sessionsCopy = new HashSet<>(sessions);
        sessionsCopy.remove(session);
        ServerPacket disconnectPacket = new ServerPacket("Client with ip " + session.getAddress() + " left the chat!");
        sessionsCopy.forEach(receiverSession -> receiverSession.send(disconnectPacket));
    }
}