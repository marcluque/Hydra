package com.marcluque.hydra.example.client;

import com.marcluque.hydra.client.tcp.HydraTCPClient;
import com.marcluque.hydra.client.tcp.TCPClient;
import com.marcluque.hydra.example.shared.ExamplePacket;
import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.handler.listener.HydraSessionListener;
import io.netty.channel.ChannelOption;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

/**
 * Created with love by marcluque on 02.11.2017.
 */
public class ExampleClient {

    private static final Logger LOGGER = LogManager.getLogger(ExampleClient.class.getName());
    static {
        LOGGER.always();
    }

    private static Session session;

    public static void main(String[] args) {
        /*
         * The session listener is optional, that's why it's a method that may be called in the builder.
         * It adds a listener to the client and is supposed to be called when
         * a session is created (in this case, when the client connects to a server). For demonstration purposes
         * this is done via a direct instantiation (anonymous class). It's advised to do this in a separate class
         * for clearness, especially when there are other methods than just the two small from the
         * SessionListener interface.
         */

        // The builder returns a session which you can use for several things
        HydraTCPClient client = new TCPClient.TCPClientBuilder("localhost", 8888, new ExampleClientProtocol())
                .workerThreads(4)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .addSessionListener(new HydraSessionListener() {
                    @Override
                    public void onConnected(Session session) {
                        LOGGER.log(Level.INFO, "Connected to server!%n");
                    }

                    @Override
                    public void onDisconnected(Session session) {
                        LOGGER.log(Level.INFO, "%nDisconnected from server!%n");
                    }
                })
                .build();

        // Checks if the client is connected to its remote host (not obligatory)
        if (client.isConnected()) {
            // Returns the session that was created for the client and its remote host
            session = client.getSession();
            LOGGER.log(Level.INFO, "%nClient is online!%n");
            LOGGER.log(Level.INFO, "Socket address: {}%n", session.getAddress());
        }

        /* Send a packet to the server via the session the client has saved */
        // Sends a String, that is converted to an object and an array, the type of the array is defined in ExamplePacket.class
        session.send(new ExamplePacket("This is a message", new String[]{"This", "is", "a", "message in an array"}));
        // Sends a list, that is converted to an object and the array, like above
        session.send(new ExamplePacket(Arrays.asList("This", "is", "a", "message", "in a list"), new String[]{"This", "is", "a", "message", "in an array"}));
        /* Sends an object the user wants to send with the limitation that the object has to be serializable.
         * Hydra internally uses a standard packet that comes ready out of the box. The only thing that is important to notice
         * is the fact, that the Handler for the packet still has to be created by the user itself. See
         * the ExampleClientPacketListener of the server example classes.
         */
        session.send("This is a String and dealt with as object by Hydra");

        // Closes the connection and releases all occupied resources
        //client.close();
    }
}