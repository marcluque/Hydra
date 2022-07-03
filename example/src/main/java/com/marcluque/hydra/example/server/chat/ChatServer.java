package com.marcluque.hydra.example.server.chat;

import com.marcluque.hydra.example.shared.chat.ServerPacket;
import com.marcluque.hydra.server.HydraServer;
import com.marcluque.hydra.server.Server;
import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.handler.listener.HydraSessionListener;
import io.netty.channel.ChannelOption;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with love by marcluque on 11.04.18
 */
public class ChatServer {

    private static final Logger LOGGER = LogManager.getLogger(ChatServer.class.getName());

    public static final Set<Session> SESSIONS = new HashSet<>();

    public static void main(String[] args) {
        HydraServer hydraServer = new Server.Builder("localhost", 8888, new ChatServerProtocol())
                .addListener(new HydraSessionListener() {
                    @Override
                    public void onConnected(Session session) {
                        LOGGER.log(Level.INFO, String.format("User with ip %s connected!%n", session.getAddress()));
                        session.send(new ServerPacket("Welcome at localhost user!"));
                        for (Session s : SESSIONS) {
                            if (!s.equals(session)) {
                                s.send(new ServerPacket("Client with ip %s connected to the chat!".formatted(session.getAddress())));
                            }
                        }
                        SESSIONS.add(session);
                    }

                    @Override
                    public void onDisconnected(Session session) {
                        LOGGER.log(Level.INFO, String.format("User with ip %s disconnected!%n", session.getAddress()));
                        for (Session s : SESSIONS) {
                            if (!s.equals(session)) {
                                s.send(new ServerPacket("Client with ip %s left the chat!".formatted(session.getAddress())));
                            }
                        }
                        SESSIONS.remove(session);
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 200)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .build();

        LOGGER.log(Level.INFO, "Server started!");
    }
}