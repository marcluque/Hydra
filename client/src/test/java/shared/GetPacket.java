package shared;

import de.datasec.hydra.shared.protocol.packets.Packet;
import de.datasec.hydra.shared.protocol.packets.PacketId;

import java.io.IOException;

/**
 * Created by DataSec on 01.10.2017.
 */
@PacketId(2)
public class GetPacket extends Packet {

    private Object object;

    public GetPacket() {
        // Important for protocol
    }

    public GetPacket(Object object) {
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
            writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getObject() {
        return object;
    }
}
