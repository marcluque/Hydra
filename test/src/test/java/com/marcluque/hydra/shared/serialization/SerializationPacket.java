package com.marcluque.hydra.shared.serialization;

import com.marcluque.hydra.shared.protocol.packets.Packet;
import com.marcluque.hydra.shared.protocol.packets.PacketId;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;

/**
 * Created with love by marcluque on 28.03.19
 */
@PacketId(1)
public class SerializationPacket extends Packet {

    private CustomClass customClass;

    private CustomClass[] customClasses;

    public SerializationPacket() {}

    public SerializationPacket(CustomClass customClass) {
        this.customClass = customClass;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        customClass = readCustomObject(byteBuf);
        customClasses = readCustomClassArray(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeCustomObject(byteBuf, customClass);
        writeCustomClassArray(byteBuf, new CustomClass[]{customClass, customClass});
    }

    public CustomClass getCustomClass() {
        return customClass;
    }

    public CustomClass[] getCustomClasses() {
        return customClasses;
    }

    @Override
    public String toString() {
        return "SerializationPacket{" +
                "customClass=" + customClass +
                "customClasses=" + Arrays.toString(customClasses) +
                '}';
    }
}