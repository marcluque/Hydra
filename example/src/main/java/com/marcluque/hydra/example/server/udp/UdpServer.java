package com.marcluque.hydra.example.server.udp;

import com.marcluque.hydra.server.udp.HydraUDPServer;
import com.marcluque.hydra.server.udp.UDPServer;
import io.netty.channel.ChannelOption;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Created with <3 by marcluque on 17.12.19
 */
public class UdpServer {

    private static final Logger LOGGER = LogManager.getLogger(UdpServer.class.getName());

    public static void main(String[] args) {
        HydraUDPServer udpServer = new UDPServer.Builder(8888, new UDPServerProtocol())
                                                .option(ChannelOption.SO_BROADCAST, true)
                                                .build();

        LOGGER.log(Level.INFO, "Server is active: {}%n", udpServer.isActive());
        LOGGER.log(Level.INFO, "Address server was bound to: {}%n", udpServer.getLocalAddress());
        LOGGER.log(Level.INFO, "Server's channel: {}%n", udpServer.channel());
        LOGGER.log(Level.INFO, "Server's session: {}%n", udpServer.udpSession());
    }
}