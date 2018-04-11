package de.datasecs.hydra.example.server.chat;

import de.datasecs.hydra.example.shared.chat.MessagePacket;
import de.datasecs.hydra.example.shared.chat.ServerPacket;
import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.protocol.HydraProtocol;

import java.util.Set;

/**
 * Created with love by DataSecs on 11.04.18
 */
public class ChatServerProtocol extends HydraProtocol {

    public ChatServerProtocol(Set<Session> sessions) {
        registerPacket(MessagePacket.class);
        registerPacket(ServerPacket.class);
        registerListener(new ChatServerPacketListener(sessions));
    }
}