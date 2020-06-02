package de.datasecs.hydra.example.client.udp;

import de.datasecs.hydra.client.Client;
import de.datasecs.hydra.client.HydraClient;
import de.datasecs.hydra.shared.handler.Session;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.SocketUtils;

/*
 * Created with <3 by DataSecs on 17.12.19
 */
public class UdpClient {

    private static Session session;

    public static void main(String[] args) {
        HydraClient client = new Client.Builder("localhost", 8888, new UdpClientProtocol())
                .useUDP(true)
                .option(ChannelOption.SO_BROADCAST, true)
                .build();

        if (client.isConnected()) {
            session = client.getSession();
            System.out.println("\nClient is online!");
            System.out.printf("Socket address: %s%n", session.getAddress());
        }

        System.out.println(session);

        try {
            session.getChannel().writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("QOTM?", CharsetUtil.UTF_8),
                    SocketUtils.socketAddress("localhost", 8888))).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Send something simple
        session.send("This is a String and dealt with as object by Hydra");
    }
}