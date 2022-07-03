package com.marcluque.hydra.example.client.chat;

import com.marcluque.hydra.client.Client;
import com.marcluque.hydra.client.HydraClient;
import com.marcluque.hydra.example.shared.chat.MessagePacket;
import com.marcluque.hydra.example.shared.chat.ServerPacket;
import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.handler.impl.UDPSession;
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
        HydraClient hydraClient = new Client.Builder("localhost", 8888, new ChatClientProtocol())
                .addSessionListener(new HydraSessionListener() {
                    @Override
                    public void onConnected(Session session) {
                        System.out.println("You are connected to the server!");
                    }

                    @Override
                    public void onDisconnected(Session session) {
                        System.out.printf("You were disconnected from the server with ip: %s%n", session.getAddress());
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
                    System.out.println("Disconnecting from chat...");
                    hydraClient.send(new ServerPacket("disconnect"));
                    hydraClient.close();
                    System.out.println("Disconnected!");
                    return;
                }

                messagePacket.setMessage(String.format("%s;%s;%s", hydraClient.getLocalAddress(), Calendar.getInstance().getTime(), input));
                hydraClient.send(messagePacket);
            } catch (IOException e) {
                LOGGER.log(Level.WARN, e);
            }
        }
    }
}