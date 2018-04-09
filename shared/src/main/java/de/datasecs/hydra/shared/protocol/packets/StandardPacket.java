package de.datasecs.hydra.shared.protocol.packets;

/**
 * Created with love by DataSecs on 09.04.18
 */
@PacketId(Byte.MIN_VALUE)
public class StandardPacket extends Packet {

    private Object object;

    public StandardPacket() {}

    public StandardPacket(Object object) {
        this.object = object;
    }

    @Override
    public void read() {
        object = readObject();
    }

    @Override
    public void write() {
        writeObject(object);
    }

    @Override
    public String toString() {
        return object.toString();
    }
}