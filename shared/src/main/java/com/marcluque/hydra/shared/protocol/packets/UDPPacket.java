package com.marcluque.hydra.shared.protocol.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.net.InetSocketAddress;

public abstract class UDPPacket extends Packet {

    protected InetSocketAddress recipient;

    protected int packetSize;

    public abstract InetSocketAddress getRecipient();

    public void setRecipient(InetSocketAddress recipient) {
        this.recipient = recipient;
    }

    public ByteBuf getByteBuf(Byte packetId) {
        ByteBuf byteBuf = Unpooled.buffer(packetSize);
        // First sent byte encodes packet id
        byteBuf.writeByte(packetId);
        // Then send the rest of the bytes with user defined write method
        write(byteBuf);
        return byteBuf;
    }
}