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

        LOGGER.log(Level.INFO, String.format("Server is active: %s\n", udpServer.isActive()));
        LOGGER.log(Level.INFO, String.format("Address server was bound to: %s\n", udpServer.getLocalAddress()));
        LOGGER.log(Level.INFO, String.format("Server's channel: %s\n", udpServer.channel()));
        LOGGER.log(Level.INFO, String.format("Server's session: %s\n", udpServer.udpSession()));
    }
}