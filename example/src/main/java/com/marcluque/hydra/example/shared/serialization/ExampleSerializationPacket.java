package com.marcluque.hydra.example.shared.serialization;

import com.marcluque.hydra.shared.protocol.packets.Packet;
import com.marcluque.hydra.shared.protocol.packets.PacketId;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with love by marcluque on 12.02.18
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
        // This method allows the user to receive a custom classes
        customClass = readCustomObject(byteBuf);
        // This method allows the user to receive an array of custom classes
        customClasses = readCustomClassArray(byteBuf);
        // This method allows the user to receive a collection of custom classes
        Collection<CustomClass> l = readCustomClassCollection(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        // Is used just like any other writing method
        writeCustomObject(byteBuf, customClass);

        // This method allows the user to send an array of custom classes
        writeCustomClassArray(byteBuf, new CustomClass[]{customClass, customClass});

        // Send a collection of custom classes
        List<CustomClass> l = new LinkedList<>();
        l.add(customClass);
        l.add(customClass);
        l.add(customClass);
        writeCustomClassCollection(byteBuf, l);
    }

    // Auto-generated toString method by IntelliJ for example purposes
    @Override
    public String toString() {
        return "ExampleSerializationPacket {" + "\n" +
                "  customClass=" + customClass + "\n" +
                "  customClasses=" + Arrays.toString(customClasses) + "\n" +
                '}';
    }
}