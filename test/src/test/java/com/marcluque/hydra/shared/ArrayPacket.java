package com.marcluque.hydra.shared;

import com.marcluque.hydra.shared.protocol.packets.Packet;
import com.marcluque.hydra.shared.protocol.packets.PacketId;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;

/**
 * Created by marcluque on 01.06.2020.
 */
@PacketId(8)
public class ArrayPacket extends Packet {

    private String[] strings;

    public ArrayPacket() {}

    public ArrayPacket(String[] strings) {
        this.strings = strings;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        strings = readArray(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeArray(byteBuf, strings);
    }


    public String[] getStrings() {
        return strings;
    }

    @Override
    public String toString() {
        return "TestPacket{" +
                "strings=" + Arrays.toString(strings) +
                '}';
    }
}