package de.datasec.hydra.shared.protocol.packets.serialization;

import de.datasec.hydra.shared.protocol.HydraProtocol;
import de.datasec.hydra.shared.protocol.packets.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Created by DataSec on 29.09.2017.
 */
public class PacketEncoder extends MessageToMessageEncoder<Packet> {

    private HydraProtocol protocol;

    public PacketEncoder(HydraProtocol protocol) {
        this.protocol = protocol;
    }

    @Override
    protected void encode(ChannelHandlerContext context, Packet packet, List<Object> out) throws Exception {
        ByteBuf byteBuf = context.alloc().buffer();
        byteBuf.writeByte(protocol.getPacketId(packet));
        packet.setByteBuf(byteBuf);
        packet.write();
        out.add(byteBuf);
    }
}
