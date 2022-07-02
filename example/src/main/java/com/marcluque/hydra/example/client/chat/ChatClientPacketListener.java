package com.marcluque.hydra.example.client.chat;

import com.marcluque.hydra.example.shared.chat.MessagePacket;
import com.marcluque.hydra.example.shared.chat.ServerPacket;
import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.protocol.packets.listener.HydraPacketListener;
import com.marcluque.hydra.shared.protocol.packets.listener.PacketHandler;

/**
 * Created with love by marcluque on 11.04.18
 */
public class ChatClientPacketListener implements HydraPacketListener {

    @SuppressWarnings("unused") // Methods annotated with @PacketHandler are called at runtime by Hydra
    @PacketHandler
    public void onMessagePacket(MessagePacket messagePacket, Session session) {
        // Message structure is: "ip;date;message" -> displayed as: date | [ip]: message
        String[] messages = messagePacket.getMessage().split(";");
        System.out.println(messages[1] + " | [" + messages[0] + "]: " + messages[2]);
    }

    @SuppressWarnings("unused") // Methods annotated with @PacketHandler are called at runtime by Hydra
    @PacketHandler
    public void onServerPacket(ServerPacket serverPacket, Session session) {
        System.out.println("[SERVER]: " + serverPacket.getMessage());
    }
}