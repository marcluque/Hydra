package com.marcluque.hydra.example.client.chat;

import com.marcluque.hydra.client.tcp.TCPClient;
import com.marcluque.hydra.client.tcp.HydraTCPClient;
import com.marcluque.hydra.example.shared.chat.MessagePacket;
import com.marcluque.hydra.example.shared.chat.ServerPacket;
import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.handler.listener.HydraSessionListener;
import io.netty.channel.ChannelOption;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

/**
 * Created with love by marcluque on 11.04.18
 */
public class ChatClient {

    private static final Logger LOGGER = LogManager.getLogger(ChatClient.class.getName());

    public static void main(String[] args) {
        HydraTCPClient hydraTCPClient = new TCPClient.TCPClientBuilder("localhost", 8888, new ChatClientProtocol())
                .addSessionListener(new HydraSessionListener() {
                    @Override
                    public void onConnected(Session session) {
                        LOGGER.log(Level.INFO, "You are connected to the server!%n");
                    }

                    @Override
                    public void onDisconnected(Session session) {
                        LOGGER.log(Level.INFO, "You were disconnected from the server with ip: {}%n",
                                session.getAddress());
                    }
                })
                .option(ChannelOption.TCP_NODELAY, true)
                .build();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        MessagePacket messagePacket = new MessagePacket();
        while (true) {
            try {
                input = bufferedReader.readLine();
                if (input.equalsIgnoreCase("#end")) {
                    LOGGER.log(Level.INFO, "Disconnecting from chat...%n");
                    hydraTCPClient.send(new ServerPacket("disconnect"));
                    hydraTCPClient.close();
                    LOGGER.log(Level.INFO, "Disconnected!%n");
                    return;
                }

                messagePacket.setMessage("%s;%s;%s".formatted(hydraTCPClient.getLocalAddress(), Calendar.getInstance().getTime(), input));
                hydraTCPClient.send(messagePacket);
            } catch (IOException e) {
                LOGGER.log(Level.WARN, e);
            }
        }
    }
}