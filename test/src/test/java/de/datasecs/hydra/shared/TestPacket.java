package de.datasecs.hydra.shared;

import de.datasecs.hydra.shared.protocol.packets.Packet;
import de.datasecs.hydra.shared.protocol.packets.PacketId;
import io.netty.buffer.ByteBuf;

/**
 * Created by DataSec on 28.03.2019.
 */
@PacketId
public class TestPacket extends Packet {

    private int number;

    private Object object;

    public TestPacket() {}

    public TestPacket(int number, Object object) {
        this.number = number;
        this.object = object;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        readInt(byteBuf);
        readObject(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeInt(byteBuf, number);
        writeObject(byteBuf, object);
    }

    public int getNumber() {
        return number;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public String toString() {
        return "TestPacket{" +
                "number=" + number +
                ", object=" + object +
                '}';
    }
}