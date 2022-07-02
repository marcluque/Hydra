package com.marcluque.hydra.example.shared.udp;

import com.marcluque.hydra.shared.protocol.packets.PacketId;
import com.marcluque.hydra.shared.protocol.packets.UDPPacket;
import io.netty.buffer.ByteBuf;

import java.net.InetSocketAddress;

@PacketId(5)
public class ExampleUDPPacket extends UDPPacket {

    private String testString;

    public ExampleUDPPacket() {
        // Empty constructor is obligatory
    }

    public ExampleUDPPacket(String testString, InetSocketAddress recipient) {
        this.testString = testString;
        super.recipient = recipient;
        // Optional: Setting the packet size is more efficient, but not required
        packetSize = testString.getBytes().length;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        testString = readString(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(byteBuf, testString);
    }

    @Override
    public InetSocketAddress getRecipient() {
        return recipient;
    }

    @Override
    public String toString() {
        return "ExampleUDPPacket{" +
                "testString='" + testString + '\'' +
                ", recipient=" + recipient +
                '}';
    }
}