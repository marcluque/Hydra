package de.datasecs.hydra.example.server.chat;

import de.datasecs.hydra.example.shared.chat.ServerPacket;
import de.datasecs.hydra.server.HydraServer;
import de.datasecs.hydra.server.Server;
import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.handler.listener.HydraSessionListener;
import io.netty.channel.ChannelOption;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with love by DataSecs on 11.04.18
 */
public class ChatServer {

    public static Set<Session> sessions = new HashSet<>();

    public static void main(String[] args) {
        HydraServer hydraServer = new Server.Builder("localhost", 8888, new ChatServerProtocol())
                .addListener(new HydraSessionListener() {
                    @Override
                    public void onConnected(Session session) {
                        System.out.printf("User with ip %s connected!%n", session.getAddress());
                        session.send(new ServerPacket("Welcome at localhost user!"));
                        for (Session s : sessions) {
                            if (!s.equals(session)) {
                                s.send(new ServerPacket("Client with ip " + session.getAddress() + " connected to the chat!"));
                            }
                        }
                        sessions.add(session);
                    }

                    @Override
                    public void onDisconnected(Session session) {
                        System.out.printf("User with ip %s disconnected!%n", session.getAddress());
                        for (Session s : sessions) {
                            if (!s.equals(session)) {
                                s.send(new ServerPacket("Client with ip " + session.getAddress() + " left the chat!"));
                            }
                        }
                        sessions.remove(session);
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 200)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .build();

        System.out.println("Server started!");
    }
}