package de.datasecs.hydra.example.client.chat;

import de.datasecs.hydra.example.shared.chat.MessagePacket;
import de.datasecs.hydra.example.shared.chat.ServerPacket;
import de.datasecs.hydra.shared.protocol.HydraProtocol;

/**
 * Created with love by DataSecs on 11.04.18
 */
public class ChatClientProtocol extends HydraProtocol {

    public ChatClientProtocol() {
        registerPacket(MessagePacket.class);
        registerPacket(ServerPacket.class);
        registerListener(new ChatClientPacketListener());
    }
}