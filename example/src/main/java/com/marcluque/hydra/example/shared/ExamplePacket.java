package com.marcluque.hydra.example.shared;

import com.marcluque.hydra.shared.protocol.packets.Packet;
import com.marcluque.hydra.shared.protocol.packets.PacketId;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;

/**
 * Created with love by marcluque on 03.11.2017.
 */
/* This is a simple example for a packet class.
 * Just the empty, public constructor and the read, write methods are obligatory.
 * The rest of the class is up to you.
 * The PacketId annotation is used to internally handle the variety of packets. The default
 * value for it is 0. Each packet is supposed to have a different id, the system of assignment is up to the user.
 */
@PacketId()
public class ExamplePacket extends Packet {

    private Object sampleObject;

    private String[] sampleStringArray;

    @SuppressWarnings("unused")
    public ExamplePacket() {
        // Hydra needs an empty constructor for packet reconstruction at runtime
    }

    // This constructor is not obligatory! Just the empty one.
    public ExamplePacket(Object sampleObject, String[] sampleStringArray) {
        this.sampleObject = sampleObject;
        this.sampleStringArray = sampleStringArray;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        sampleObject = readObject(byteBuf);
        sampleStringArray = readArray(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeObject(byteBuf, sampleObject);
        writeArray(byteBuf, sampleStringArray);
    }

    // Auto-generated toString method by IntelliJ for example purposes
    @Override
    public String toString() {
        return "ExamplePacket{" +
                "sampleObject=" + sampleObject +
                ", sampleStringArray=" + Arrays.toString(sampleStringArray) +
                '}';
    }
}