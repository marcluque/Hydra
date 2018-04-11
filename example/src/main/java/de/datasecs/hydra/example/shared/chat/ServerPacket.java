package de.datasecs.hydra.example.shared.chat;

import de.datasecs.hydra.shared.protocol.packets.Packet;
import de.datasecs.hydra.shared.protocol.packets.PacketId;

/**
 * Created with love by DataSecs on 11.04.18
 */
@PacketId(1)
public class ServerPacket extends Packet {

    private String message;

    public ServerPacket() {}

    public ServerPacket(String message) {
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

    public String getMessage() {
        return message;
    }
}