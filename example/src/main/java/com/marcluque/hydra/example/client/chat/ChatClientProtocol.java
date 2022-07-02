package com.marcluque.hydra.example.client.chat;

import com.marcluque.hydra.example.shared.chat.MessagePacket;
import com.marcluque.hydra.example.shared.chat.ServerPacket;
import com.marcluque.hydra.shared.protocol.impl.HydraProtocol;

/**
 * Created with love by marcluque on 11.04.18
 */
public class ChatClientProtocol extends HydraProtocol {

    public ChatClientProtocol() {
        registerPacket(MessagePacket.class);
        registerPacket(ServerPacket.class);
        registerListener(new ChatClientPacketListener());
    }
}