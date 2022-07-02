package com.marcluque.hydra.example.server.chat;

import com.marcluque.hydra.example.shared.chat.MessagePacket;
import com.marcluque.hydra.example.shared.chat.ServerPacket;
import com.marcluque.hydra.shared.protocol.impl.HydraProtocol;

/**
 * Created with love by marcluque on 11.04.18
 */
public class ChatServerProtocol extends HydraProtocol {

    public ChatServerProtocol() {
        registerPacket(MessagePacket.class);
        registerPacket(ServerPacket.class);
        registerListener(new ChatServerPacketListener());
    }
}