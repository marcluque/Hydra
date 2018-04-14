package de.datasecs.hydra.example.shared.serialization;

import de.datasecs.hydra.shared.protocol.packets.Packet;
import de.datasecs.hydra.shared.protocol.packets.PacketId;

import java.util.Arrays;

/**
 * Created with love by DataSecs on 12.02.18
 */
@PacketId()
public class ExampleSerializationPacket extends Packet {

    private CustomClass customClass;

    private CustomClass[] customClasses;

    public ExampleSerializationPacket() {}

    public ExampleSerializationPacket(CustomClass customClass) {
        this.customClass = customClass;
    }

    @Override
    public void read() {
        customClass = readCustomObject();
        customClasses = readCustomClassArray();
    }

    @Override
    public void write() {
        /* The string 'pathOfCustomClassAtReceiver' is used to determine what the package name of the custom class
         * is, at the server side. As the package names from client and server very likely differ (actually should differ),
         * there is this string to determine the package where ALL the classes that are related to each other and have to
         * be serialized are inside. Therefore it's necessary to put all related classes that are supposed to be serialized
         * together in a package. This is the only (big) drawback.
         */
        writeCustomObject(customClass, "de.datasecs.hydra.example.shared.serialization");

        // This method allows the user to send an array of custom classes. This method also needs the 'pathOfCustomClassAtReceiver'
        writeCustomClassArray(new CustomClass[]{customClass, customClass}, "de.datasecs.hydra.example.shared.serialization");
    }

    // Auto-generated toString method by IntelliJ for example purposes
    @Override
    public String toString() {
        return "ExampleSerializationPacket{" +
                "customClass=" + customClass +
                "customClasses=" + Arrays.toString(customClasses) +
                '}';
    }
}