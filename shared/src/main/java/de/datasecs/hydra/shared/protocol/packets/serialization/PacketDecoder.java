package de.datasecs.hydra.shared.protocol.packets.serialization;

import de.datasecs.hydra.shared.protocol.Protocol;
import de.datasecs.hydra.shared.protocol.packets.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * Created with love by DataSecs on 29.09.2017.
 */
public class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {

    private Protocol protocol;

    public PacketDecoder(Protocol protocol) {
        this.protocol = protocol;
    }

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf in, List<Object> out) {
        int length = in.readInt();

        if (length > 0) {
            Packet packet = protocol.createPacket(in.readByte());
            packet.read(in);
            out.add(packet);
        }
    }
}