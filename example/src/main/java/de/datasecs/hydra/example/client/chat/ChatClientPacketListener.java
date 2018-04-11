package de.datasecs.hydra.example.client.chat;

import de.datasecs.hydra.example.shared.chat.MessagePacket;
import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.protocol.packets.listener.HydraPacketListener;
import de.datasecs.hydra.shared.protocol.packets.listener.PacketHandler;

/**
 * Created with love by DataSecs on 11.04.18
 */
public class ChatClientPacketListener implements HydraPacketListener {

    @PacketHandler
    public void onMessagePacket(MessagePacket messagePacket, Session session) {

    }
}
