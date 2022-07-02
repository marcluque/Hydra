package com.marcluque.hydra.shared.serialization;

import com.marcluque.hydra.shared.protocol.packets.Packet;
import com.marcluque.hydra.shared.protocol.packets.PacketId;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;

/**
 * Created with love by marcluque on 28.03.19
 */
@PacketId(1)
public class ExampleSerializationPacket extends Packet {

    private CustomClass customClass;

    private CustomClass[] customClasses;

    public ExampleSerializationPacket() {}

    public ExampleSerializationPacket(CustomClass customClass) {
        this.customClass = customClass;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        customClass = readCustomObject(byteBuf);
        customClasses = readCustomClassArray(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeCustomObject(byteBuf, customClass, "com.marcluque.hydra.shared.serialization");
        writeCustomClassArray(byteBuf, new CustomClass[]{customClass, customClass}, "com.marcluque.hydra.shared.serialization");
    }

    @Override
    public String toString() {
        return "ExampleSerializationPacket{" +
                "customClass=" + customClass +
                "customClasses=" + Arrays.toString(customClasses) +
                '}';
    }
}