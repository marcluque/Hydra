package com.marcluque.hydra.example.client;

import com.marcluque.hydra.example.shared.ExamplePacket;
import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.protocol.packets.listener.HydraPacketListener;
import com.marcluque.hydra.shared.protocol.packets.listener.PacketHandler;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created with love by marcluque on 03.11.2017.
 */
public class ExampleClientPacketListener implements HydraPacketListener {

    private static final Logger LOGGER = LogManager.getLogger(ExampleClientPacketListener.class.getName());

    public ExampleClientPacketListener() {
        // Do something
    }

    /* Use the @PacketHandler annotation on methods that are supposed to handle packets.
     * The amount of parameters has always to be equal 2 and in the given order. (Packet class and then the session)
     */
    @SuppressWarnings("unused") // Methods annotated with @PacketHandler are called at runtime by Hydra
    @PacketHandler
    public void onSamplePacket(ExamplePacket examplePacket, Session session) {
        LOGGER.log(Level.INFO, "%n---PACKET-LISTENER OUTPUT---");

        // Process received packet
        LOGGER.log(Level.INFO, String.format("Received from server: %s%n", examplePacket));

        // Send response
        //session.send(new ExamplePacket("This is a response", new String[]{"This", "is", "a", "response"}));

        // Returns if the session is active
        LOGGER.log(Level.INFO, "%nIs session active?: %s".formatted(session.isConnected()));

        // Returns the local or remote address, depending on if it's the server or the client
        LOGGER.log(Level.INFO, "Remote address (client is connected to): %s".formatted(session.getAddress()));

        // Closes the session, this does not stop the client. It just closes the channel!
        session.close();
        LOGGER.log(Level.INFO, "%nSession closed!");

        // Check again if session is active
        LOGGER.log(Level.INFO, "Is session active?: %s".formatted(session.isConnected()));
    }
}