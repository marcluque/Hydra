package client;

import client.serialization.CustomClass;
import de.datasec.hydra.shared.protocol.packets.Packet;
import de.datasec.hydra.shared.protocol.packets.PacketId;

import java.util.Arrays;

/**
 * Created with love by DataSec on 03.11.2017.
 */
/* This is a simple example for a packet class.
 * Just the empty, public constructor and the read, write methods are obligatory.
 * The rest of the class is up to you.
 * The PacketId annotation is used to internally handle the variety of packets. The default
 * value for it is 0. Each packet is supposed to have a different id, the system of assignment is up to the user.
 */
@PacketId(0)
public class SamplePacket extends Packet {

    private Object sampleObject;

    private String[] sampleStringArray;

    private CustomClass customObject;

    public SamplePacket() {
        // Empty constructor is always necessary!
    }

    // This constructor is not obligatory! Just the empty one.
    public SamplePacket(Object sampleObject, String[] sampleStringArray, CustomClass customObject) {
        this.sampleObject = sampleObject;
        this.sampleStringArray = sampleStringArray;
        this.customObject = customObject;
    }

    // This constructor has no senseful purpose,
    // it's just so the server can send an easy answer and doesn't need a customObject
    public SamplePacket(Object sampleObject, String[] sampleStringArray) {
        this.sampleObject = sampleObject;
        this.sampleStringArray = sampleStringArray;
    }

    @Override
    public void read() {
        sampleObject = readObject();
        sampleStringArray = readArray();
        customObject = readCustomObject(customObject);
    }

    @Override
    public void write() {
        writeObject(sampleObject);
        writeArray(sampleStringArray);
        // TODO: Explain purpose of the string and explain what the conditions of usage with this are
        writeCustomObject(customObject, "server.serialization");
    }

    public Object getSampleObject() {
        return sampleObject;
    }

    public String[] getSampleStringArray() {
        return sampleStringArray;
    }

    public CustomClass getCustomObject() {
        return customObject;
    }

    // Auto-generated toString method by IntelliJ for example purposes
    @Override
    public String toString() {
        return "SamplePacket{" +
                "sampleObject=" + sampleObject +
                ", sampleStringArray=" + Arrays.toString(sampleStringArray) +
                ", customObject=" + customObject +
                '}';
    }
}