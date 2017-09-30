package src;

import de.datasec.hydra.shared.protocol.packets.Packet;
import de.datasec.hydra.shared.protocol.packets.PacketId;

import java.io.IOException;

/**
 * Created by DataSec on 30.09.2017.
 */
@PacketId(1)
public class SimplePacket extends Packet {

    private Object object;

    public SimplePacket() {}

    public SimplePacket(Object object) {
        this.object = object;
    }

    @Override
    public void read() {
        try {
            object = readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write() {
        try {
            writeObject("HAY");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getObject() {
        return object;
    }
}
