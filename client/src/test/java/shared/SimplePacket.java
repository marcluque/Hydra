package shared;

import de.datasec.hydra.shared.protocol.packets.Packet;
import de.datasec.hydra.shared.protocol.packets.PacketId;

import java.io.IOException;

/**
 * Created by DataSec on 30.09.2017.
 */
@PacketId(1)
public class SimplePacket extends Packet {

    private Object string;

    public SimplePacket() {}

    public SimplePacket(String string) {
        this.string = string;
    }

    @Override
    public void read() {

        try {
            string = readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void write() {
        try {
            writeObject(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getString() {
        return string;
    }
}
