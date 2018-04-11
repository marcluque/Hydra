package de.datasecs.hydra.example.shared.chat;

import de.datasecs.hydra.shared.protocol.packets.Packet;
import de.datasecs.hydra.shared.protocol.packets.PacketId;

/**
 * Created with love by DataSecs on 11.04.18
 */
@PacketId()
public class MessagePacket extends Packet {

    private String message;

    public MessagePacket() {}

    public MessagePacket(String message) {
        this.message = message;
    }

    @Override
    public void read() {
        message = readString();
    }

    @Override
    public void write() {
        writeString(message);
    }
}