package de.datasecs.hydra.shared;

import de.datasecs.hydra.shared.protocol.packets.Packet;
import de.datasecs.hydra.shared.protocol.packets.PacketId;
import io.netty.buffer.ByteBuf;

@PacketId(120)
public class FinishedPacket extends Packet {

    private int number;

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