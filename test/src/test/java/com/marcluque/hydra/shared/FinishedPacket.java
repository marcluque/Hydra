package com.marcluque.hydra.shared;

import com.marcluque.hydra.shared.protocol.packets.Packet;
import com.marcluque.hydra.shared.protocol.packets.PacketId;
import io.netty.buffer.ByteBuf;

@PacketId(120)
public class FinishedPacket extends Packet {

    private int number;

    @SuppressWarnings("unused")
    public FinishedPacket() {}

    public FinishedPacket(int number) {
        this.number = number;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        number = readInt(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeInt(byteBuf, number);
    }

    public int getNumber() {
        return number;
    }
}