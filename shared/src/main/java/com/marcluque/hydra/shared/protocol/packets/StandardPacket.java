package com.marcluque.hydra.shared.protocol.packets;

import io.netty.buffer.ByteBuf;

/**
 * Created with love by marcluque on 09.04.18
 */
@PacketId(Byte.MIN_VALUE)
public class StandardPacket extends Packet {

    private Object object;

    @SuppressWarnings("unused")
    public StandardPacket() {
        // Hydra needs an empty constructor for packet reconstruction at runtime
    }

    public StandardPacket(Object object) {
        this.object = object;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        object = readObject(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeObject(byteBuf, object);
    }

    public Object getObject() {
        return object;
    }

    @Override
    public String toString() {
        return object.toString();
    }
}