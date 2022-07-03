package com.marcluque.hydra.example.client.udp;

import com.marcluque.hydra.client.udp.HydraUDPClient;
import com.marcluque.hydra.client.udp.UDPClient;
import com.marcluque.hydra.example.shared.udp.ExampleUDPPacket;
import com.marcluque.hydra.shared.handler.impl.UDPSession;
import io.netty.channel.ChannelOption;
import io.netty.util.internal.SocketUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Created with <3 by marcluque on 17.12.19
 */
public class UdpClient {

    private static final Logger LOGGER = LogManager.getLogger(UdpClient.class.getName());

    public static void main(String[] args) {
        // A UDP client behaves different from a normal Hydra client; a udp client is a socket and
        // a hydra client merely a connection (session)
        HydraUDPClient udpClient = new UDPClient.Builder(8889, new UDPClientProtocol())
                                                .option(ChannelOption.SO_BROADCAST, true)
                                                .build();

        LOGGER.log(Level.INFO, "Client is active: {}%n", udpClient.isActive());
        LOGGER.log(Level.INFO, "Client's channel: {}%n", udpClient.channel());
        LOGGER.log(Level.INFO, "Address the client is bound to: {}%n", udpClient.getLocalAddress());

        UDPSession session = udpClient.udpSession();
        LOGGER.log(Level.INFO, "Session is active: {}%n", session.isActive());
        // Note that the session's channel is the same as the client's channel
        LOGGER.log(Level.INFO, "Session's channel: {}%n", session.getChannel());

        // Send something simple
        udpClient.send(new ExampleUDPPacket("This is a String", SocketUtils.socketAddress("localhost", 8888)));
    }
}