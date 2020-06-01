package de.datasecs.hydra.shared;

import de.datasecs.hydra.shared.protocol.packets.Packet;
import de.datasecs.hydra.shared.protocol.packets.PacketId;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;

/**
 * Created by DataSec on 01.06.2020.
 */
@PacketId(8)
public class ArrayPacket extends Packet {

    private int number;

    private String[] strings;

    public ArrayPacket() {}

    public ArrayPacket(int number, String[] strings) {
        this.number = number;
        this.strings = strings;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        number = readInt(byteBuf);
        strings = readArray(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeInt(byteBuf, number);
        writeArray(byteBuf, strings);
    }

    public int getNumber() {
        return number;
    }

    public String[] getStrings() {
        return strings;
    }

    @Override
    public String toString() {
        return "TestPacket{" +
                "number=" + number +
                ", object=" + Arrays.toString(strings) +
                '}';
    }
}