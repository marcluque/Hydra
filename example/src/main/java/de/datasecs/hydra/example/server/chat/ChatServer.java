package de.datasecs.hydra.example.server.chat;

import de.datasecs.hydra.example.shared.chat.MessagePacket;
import de.datasecs.hydra.server.HydraServer;
import de.datasecs.hydra.server.Server;
import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.handler.listener.HydraSessionListener;
import io.netty.channel.ChannelOption;

/**
 * Created with love by DataSecs on 11.04.18
 */
public class ChatServer {

    public static void main(String[] args) {
        HydraServer hydraServer = new Server.Builder("localhost", 8888, new ChatServerProtocol())
                .addListener(new HydraSessionListener() {
                    @Override
                    public void onConnected(Session session) {
                        System.out.printf("User with ip %s connected!%n", session.getAddress());
                        session.send(new MessagePacket("Welcome at localhost user!"));
                    }

                    @Override
                    public void onDisconnected(Session session) {
                        System.out.printf("User with ip %s disconnected!%n", session.getAddress());
                    }
                })
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_BACKLOG, 200)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_BACKLOG, 200)
                .build();
    }
}