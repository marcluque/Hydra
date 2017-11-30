package client;

import de.datasec.hydra.shared.protocol.packets.Packet;
import de.datasec.hydra.shared.protocol.packets.PacketId;

import java.io.IOException;

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

    public SamplePacket() {
        // Empty constructor is always necessary!
    }

    // This constructor is not obligatory! Just the empty one.
    public SamplePacket(Object sampleObject) {
        this.sampleObject = sampleObject;
    }

    @Override
    public void read() {
        try {
            sampleObject = readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write() {
        try {
            writeObject(sampleObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getSampleObject() {
        return sampleObject;
    }

    @Override
    public String toString() {
        return sampleObject.toString();
    }
}
