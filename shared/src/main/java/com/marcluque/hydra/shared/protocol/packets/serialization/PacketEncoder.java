package com.marcluque.hydra.shared.protocol.packets.serialization;

import com.marcluque.hydra.shared.protocol.Protocol;
import com.marcluque.hydra.shared.protocol.packets.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Created with love by marcluque on 29.09.2017.
 */
public class PacketEncoder extends MessageToMessageEncoder<Packet> {

    private final Protocol protocol;

    public PacketEncoder(Protocol protocol) {
        this.protocol = protocol;
    }

    @Override
    protected void encode(ChannelHandlerContext context, Packet packet, List<Object> out) {
        ByteBuf byteBuf = context.alloc().directBuffer();
        byteBuf.writeByte(protocol.getPacketId(packet));
        packet.write(byteBuf);
        out.add(byteBuf);
    }
}