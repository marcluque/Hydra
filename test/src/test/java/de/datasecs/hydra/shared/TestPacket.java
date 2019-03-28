package de.datasecs.hydra.shared;

import de.datasecs.hydra.shared.protocol.packets.Packet;
import de.datasecs.hydra.shared.protocol.packets.PacketId;
import io.netty.buffer.ByteBuf;

/**
 * Created by DataSec on 28.03.2019.
 */
@PacketId
public class TestPacket extends Packet {

    private Object object;

    public TestPacket() {}

    public TestPacket(Object object) {
        this.object = object;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        readObject(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeObject(byteBuf, object);
    }

    @Override
    public String toString() {
        return "TestPacket{" +
                "object=" + object +
                '}';
    }
}
