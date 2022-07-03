package com.marcluque.hydra.example.client.serialization;

import com.marcluque.hydra.client.Client;
import com.marcluque.hydra.client.HydraClient;
import com.marcluque.hydra.example.server.ExampleServer;
import com.marcluque.hydra.example.shared.serialization.CustomClass;
import com.marcluque.hydra.example.shared.serialization.CustomClassExtended;
import com.marcluque.hydra.example.shared.serialization.ExampleSerializationPacket;
import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.handler.listener.HydraSessionListener;
import io.netty.channel.ChannelOption;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created with love by marcluque on 12.02.18
 */
public class ExampleSerializationClient {

    private static final Logger LOGGER = LogManager.getLogger(ExampleSerializationClient.class.getName());

    private static Session session;

    public static void main(String[] args) {
        HydraClient client = new Client.Builder("localhost", 8888, new ExampleSerializationClientProtocol())
                .workerThreads(4)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .addSessionListener(new HydraSessionListener() {
                    @Override
                    public void onConnected(Session session) {
                        LOGGER.log(Level.INFO, "Connected to server!");
                    }

                    @Override
                    public void onDisconnected(Session session) {
                        LOGGER.log(Level.INFO, "%nDisconnected from server!");
                    }
                })
                .build();

        if (client.isConnected()) {
            session = client.getSession();
            LOGGER.log(Level.INFO, "%nClient is online!");
            LOGGER.log(Level.INFO, String.format("Socket address: %s%n", session.getAddress()));
        }

        // Create custom classes and necessary stuff for example serialization
        List<String> testStringList = new ArrayList<>();
        testStringList.add("Hydra");
        testStringList.add("Serialization");
        testStringList.add("Test");

        CustomClassExtended customClassExtended = new CustomClassExtended("testStringExtended",
                                                                                            UUID.randomUUID(),
                                                                                            null,
                                                                                            5L,
                                                                                            Integer.class);
        CustomClassExtended customClassExtended2 = new CustomClassExtended("testStringExtended",
                                                                                            UUID.randomUUID(),
                                                                                            customClassExtended,
                                                                                            5L,
                                                                                            Integer.class);
        CustomClass customClass = new CustomClass("testString",
                                                            1,
                                                            new String[]{"Hydra", "serialization"},
                                                            customClassExtended2,
                                                            testStringList,
                                                            "this is a random object");

        // Sends the instance of a custom class, that is created and filled with data above
        session.send(new ExampleSerializationPacket(customClass));
    }
}