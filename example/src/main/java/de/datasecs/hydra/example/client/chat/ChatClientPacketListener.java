package de.datasecs.hydra.example.client.chat;

import de.datasecs.hydra.example.shared.chat.MessagePacket;
import de.datasecs.hydra.example.shared.chat.ServerPacket;
import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.protocol.packets.listener.HydraPacketListener;
import de.datasecs.hydra.shared.protocol.packets.listener.PacketHandler;

/**
 * Created with love by DataSecs on 11.04.18
 */
public class ChatClientPacketListener implements HydraPacketListener {

    @PacketHandler
    public void onMessagePacket(MessagePacket messagePacket, Session session) {
        // Message structure is: "ip;date;message" -> displayed as: date | [ip]: message
        String[] messages = messagePacket.getMessage().split(";");
        System.out.println(messages[1] + " | [" + messages[0] + "]: " + messages[2]);
    }

    @PacketHandler
    public void onServerPacket(ServerPacket serverPacket, Session session) {
        System.out.println("[SERVER]: " + serverPacket.getMessage());
    }
}
