package de.datasecs.hydra.example.client.chat;

import de.datasecs.hydra.client.Client;
import de.datasecs.hydra.client.HydraClient;
import de.datasecs.hydra.example.shared.chat.MessagePacket;
import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.handler.listener.HydraSessionListener;
import io.netty.channel.ChannelOption;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created with love by DataSecs on 11.04.18
 */
public class ChatClient {

    public static void main(String[] args) {

        HydraClient hydraClient = new Client.Builder("localhost", 8888, new ChatClientProtocol())
                .addSessionListener(new HydraSessionListener() {
                    @Override
                    public void onConnected(Session session) {
                        System.out.printf("You are connected to the server with ip: %s%n", session.getAddress());
                    }

                    @Override
                    public void onDisconnected(Session session) {
                        System.out.printf("You were disconnected from the server with ip: %s%n", session.getAddress());
                    }
                })
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_BACKLOG, 200)
                .build();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                hydraClient.send(new MessagePacket(String.format("%s\r\n", bufferedReader.readLine())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}