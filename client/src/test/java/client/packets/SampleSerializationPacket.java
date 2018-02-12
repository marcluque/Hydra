package client.packets;

import client.serialization.CustomClass;
import de.datasec.hydra.shared.protocol.packets.Packet;
import de.datasec.hydra.shared.protocol.packets.PacketId;

/**
 * Created with love by DataSec on 12.02.18
 */
// The id now is 1 instead of 0 or empty like for the sample packet, as they have to differ
@PacketId(1)
public class SampleSerializationPacket extends Packet {

    private CustomClass customClass;

    public SampleSerializationPacket() {}

    public SampleSerializationPacket(CustomClass customClass) {
        this.customClass = customClass;
    }

    @Override
    public void read() {
        customClass = readCustomObject(customClass);
    }

    @Override
    public void write() {
        /* The string 'pathOfCustomClassAtReceiver' is used to determine what the package name of the custom class
         * is, at the server side. As the package names from client and server very likely differ (actually should differ),
         * there is this string to determine the package where ALL the classes that are related to each other and have to
         * be serialized are inside. Therefore it's necessary to put all related classes that are supposed to be serialized
         * together in a package. This is the only big drawback.
         */
        writeCustomObject(customClass, "server.serialization");
    }

    // Auto-generated toString method by IntelliJ for example purposes
    @Override
    public String toString() {
        return "SampleSerializationPacket{" +
                "customClass=" + customClass +
                '}';
    }
}