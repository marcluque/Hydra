package com.marcluque.hydra.example.server.udp;

import com.marcluque.hydra.server.udp.HydraUDPServer;
import com.marcluque.hydra.server.udp.UDPServer;
import io.netty.channel.ChannelOption;

/*
 * Created with <3 by marcluque on 17.12.19
 */
public class UdpServer {

    public static void main(String[] args) {
        HydraUDPServer udpServer = new UDPServer.Builder(8888, new UDPServerProtocol())
                                                .option(ChannelOption.SO_BROADCAST, true)
                                                .build();

        System.out.printf("Server is active: %s%n", udpServer.isActive());
        System.out.printf("Address server was bound to: %s%n", udpServer.getLocalAddress());
        System.out.printf("Server's channel: %s%n", udpServer.channel());
        System.out.printf("Server's session: %s%n", udpServer.udpSession());
    }
}