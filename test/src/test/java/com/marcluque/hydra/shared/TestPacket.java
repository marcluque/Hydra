package com.marcluque.hydra.shared;

import com.marcluque.hydra.shared.protocol.packets.Packet;
import com.marcluque.hydra.shared.protocol.packets.PacketId;
import io.netty.buffer.ByteBuf;

/**
 * Created by marcluque on 28.03.2019.
 */
@PacketId(2)
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
        number = readInt(byteBuf);
        object = readObject(byteBuf);
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