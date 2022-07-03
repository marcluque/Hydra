package com.marcluque.hydra.example.server.chat;

import com.marcluque.hydra.example.shared.chat.MessagePacket;
import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.protocol.packets.listener.HydraPacketListener;
import com.marcluque.hydra.shared.protocol.packets.listener.PacketHandler;

/**
 * Created with love by marcluque on 11.04.18
 */
public class ChatServerPacketListener implements HydraPacketListener {

    @SuppressWarnings("unused") // Methods annotated with @PacketHandler are called at runtime by Hydra
    @PacketHandler
    public void onMessagePacket(MessagePacket messagePacket, Session session) {
        for (Session s : ChatServer.SESSIONS) {
            if (!s.equals(session)) {
                s.send(messagePacket);
            }
        }
    }
}