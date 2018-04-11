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
import java.util.Calendar;

/**
 * Created with love by DataSecs on 11.04.18
 */
public class ChatClient {

    public static void main(String[] args) {
        HydraClient hydraClient = new Client.Builder("localhost", 8888, new ChatClientProtocol())
                .addSessionListener(new HydraSessionListener() {
                    @Override
                    public void onConnected(Session session) {
                        //TODO: Find out why getAddress() returns null
                        System.out.printf("You are connected to the server with ip: %s%n", session.getAddress());
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
                if (input.equalsIgnoreCase("#end\r\n")) {
                    System.out.println("Disconnecting from chat...");
                    hydraClient.close();
                    System.out.println("Disconnected!");
                    return;
                }

                messagePacket.setMessage(String.format("%s;%s;%s\r\n", hydraClient.getLocalAddress(), Calendar.getInstance().getTime().toString(), input));
                hydraClient.send(messagePacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}