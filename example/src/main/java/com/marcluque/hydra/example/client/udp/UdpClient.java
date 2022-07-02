package com.marcluque.hydra.example.client.udp;

import com.marcluque.hydra.client.udp.HydraUDPClient;
import com.marcluque.hydra.client.udp.UDPClient;
import com.marcluque.hydra.example.shared.udp.ExampleUDPPacket;
import com.marcluque.hydra.shared.handler.impl.UDPSession;
import io.netty.channel.ChannelOption;
import io.netty.util.internal.SocketUtils;

/*
 * Created with <3 by marcluque on 17.12.19
 */
public class UdpClient {

    public static void main(String[] args) {
        // A UDP client behaves different from a normal Hydra client; a udp client is a socket and
        // a hydra client merely a connection (session)
        HydraUDPClient udpClient = new UDPClient.Builder(8889, new UDPClientProtocol())
                                                .option(ChannelOption.SO_BROADCAST, true)
                                                .build();

        System.out.printf("Client is active: %s%n", udpClient.isActive());
        System.out.printf("Client's channel: %s%n", udpClient.channel());
        System.out.printf("Address the client is bound to: %s%n", udpClient.getLocalAddress());

        UDPSession session = udpClient.udpSession();
        System.out.printf("Session is active: %s%n", session.isActive());
        // Note that the session's channel is the same as the client's channel
        System.out.println("Session's channel: " + session.getChannel());

        // Send something simple
        udpClient.send(new ExampleUDPPacket("This is a String", SocketUtils.socketAddress("localhost", 8888)));
    }
}