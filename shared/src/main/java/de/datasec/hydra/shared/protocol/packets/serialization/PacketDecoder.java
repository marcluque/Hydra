package de.datasec.hydra.shared.protocol.packets.serialization;

import de.datasec.hydra.shared.protocol.HydraProtocol;
import de.datasec.hydra.shared.protocol.packets.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * Created by DataSec on 29.09.2017.
 */
public class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {

    private HydraProtocol protocol;

    public PacketDecoder(HydraProtocol protocol) {
        this.protocol = protocol;
    }

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf in, List<Object> out) throws Exception {
        in.readInt();

        Packet packet = protocol.createPacket(in.readByte());
        packet.setByteBuf(in);
        packet.read();
        out.add(packet);
    }
}