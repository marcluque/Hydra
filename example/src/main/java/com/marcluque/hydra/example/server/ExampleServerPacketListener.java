package com.marcluque.hydra.example.server;

import com.marcluque.hydra.example.client.chat.ChatClientPacketListener;
import com.marcluque.hydra.example.shared.ExamplePacket;
import com.marcluque.hydra.example.shared.serialization.ExampleSerializationPacket;
import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.protocol.packets.StandardPacket;
import com.marcluque.hydra.shared.protocol.packets.listener.HydraPacketListener;
import com.marcluque.hydra.shared.protocol.packets.listener.PacketHandler;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created with love by marcluque on 03.11.2017.
 */
public class ExampleServerPacketListener implements HydraPacketListener {

    private static final Logger LOGGER = LogManager.getLogger(ExampleServerPacketListener.class.getName());


    public ExampleServerPacketListener() {
        // Do something
    }

    /* Use the @PacketHandler annotation on methods that are supposed to handle packets.
     * The amount of parameters has always to be equal 2 and in the given order. (Packet class and then the session)
     */
    @SuppressWarnings("unused") // Methods annotated with @PacketHandler are called at runtime by Hydra
    @PacketHandler
    public void onSamplePacket(ExamplePacket examplePacket, Session session) {
        LOGGER.log(Level.INFO, "\n---PACKET-LISTENER OUTPUT---");

        // Process received packet
        LOGGER.log(Level.INFO, String.format("Received from client: %s%n", examplePacket));

        // Send response
        session.send(new ExamplePacket("This is a response", new String[]{"This", "is", "a", "response"}));

        // Returns whether the session is active
        LOGGER.log(Level.INFO, "\nIs session active?: " + session.isConnected());

        // Returns the local or remote address, depending on if it's the server or the client
        LOGGER.log(Level.INFO, "Local server address: " + session.getAddress());

        // TODO: See if it can be checked whether the session is already closed and return a boolean for that
        // Closes the session, this does not stop the server. It just closes the channel!
        session.close();
        LOGGER.log(Level.INFO, "\nSession closed!");

        // Check again if session is active
        LOGGER.log(Level.INFO, "Is session active?: " + session.isConnected());
    }

    @SuppressWarnings("unused") // Methods annotated with @PacketHandler are called at runtime by Hydra
    @PacketHandler
    public void onSampleSerializationPacket(ExampleSerializationPacket exampleSerializationPacket, Session session) {
        LOGGER.log(Level.INFO, "\n---PACKET-LISTENER OUTPUT---");

        LOGGER.log(Level.INFO, String.format("Received from client: %s%n", exampleSerializationPacket));

        session.close();
        LOGGER.log(Level.INFO, "\nSession closed!");
    }

    @SuppressWarnings("unused") // Methods annotated with @PacketHandler are called at runtime by Hydra
    @PacketHandler
    public void onStandardPacket(StandardPacket standardPacket, Session session) {
        LOGGER.log(Level.INFO, "\n---PACKET-LISTENER OUTPUT---");

        LOGGER.log(Level.INFO, String.format("Received from client using the StandardPacket: %s%n", standardPacket));

        session.close();
        LOGGER.log(Level.INFO, "\nSession closed!");
    }
}