package de.datasecs.hydra.example.client.serialization;

import de.datasecs.hydra.client.Client;
import de.datasecs.hydra.client.HydraClient;
import de.datasecs.hydra.example.shared.ExampleProtocol;
import de.datasecs.hydra.example.shared.packets.ExampleSerializationPacket;
import de.datasecs.hydra.example.shared.serialization.CustomClass;
import de.datasecs.hydra.example.shared.serialization.CustomClassExtended;
import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.handler.listener.HydraSessionListener;
import io.netty.channel.ChannelOption;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created with love by DataSecs on 12.02.18
 */
public class ExampleSerializationClient {

    private static Session session;

    public static void main(String[] args) {
        HydraClient client = new Client.Builder("localhost", 8888, new ExampleProtocol())
                .workerThreads(4)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .addSessionListener(new HydraSessionListener() {
                    @Override
                    public void onConnected(Session session) {
                        System.out.println("Connected to server!");
                    }

                    @Override
                    public void onDisconnected(Session session) {
                        System.out.println("\nDisconnected from server!");
                    }
                })
                .build();

        if (client.isConnected()) {
            session = client.getSession();
            System.out.println("\nClient is online!");
            System.out.printf("Socket address: %s%n", session.getAddress());
        }

        // Create custom classes and necessary stuff for example serialization
        List<String> testStringList = new ArrayList<>();
        testStringList.add("Hydra");
        testStringList.add("Serialization");
        testStringList.add("Test");

        CustomClassExtended customClassExtended = new CustomClassExtended("testStringExtended", UUID.randomUUID(), 5L, Integer.class);
        CustomClass customClass = new CustomClass("testString", 1, new String[]{"Hydra", "serialization"}, testStringList, "this is a random object", customClassExtended);

        // Sends the instance of a custom class, that is create and filled with data above
        session.send(new ExampleSerializationPacket(customClass));
    }
}