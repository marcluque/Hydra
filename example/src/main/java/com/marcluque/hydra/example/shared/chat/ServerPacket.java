package com.marcluque.hydra.example.shared.chat;

import com.marcluque.hydra.shared.protocol.packets.Packet;
import com.marcluque.hydra.shared.protocol.packets.PacketId;
import io.netty.buffer.ByteBuf;

/**
 * Created with love by marcluque on 11.04.18
 */
@PacketId(1)
public class ServerPacket extends Packet {

    private String message;

    public ServerPacket() {}

    public ServerPacket(String message) {
        this.message = message;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        message = readString(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(byteBuf, message);
    }

    public String getMessage() {
        return message;
    }
}