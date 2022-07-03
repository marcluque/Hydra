package com.marcluque.hydra.example.server;

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

    private static final String OUTPUT_HEADER = "%n---PACKET-LISTENER OUTPUT---";

    private static final String OUTPUT_FOOTER = "%nSession closed!%n";

    public ExampleServerPacketListener() {
        // Do something
    }

    /* Use the @PacketHandler annotation on methods that are supposed to handle packets.
     * The amount of parameters has always to be equal 2 and in the given order. (Packet class and then the session)
     */
    @SuppressWarnings("unused") // Methods annotated with @PacketHandler are called at runtime by Hydra
    @PacketHandler
    public void onSamplePacket(ExamplePacket examplePacket, Session session) {
        LOGGER.log(Level.INFO, OUTPUT_HEADER);

        // Process received packet
        LOGGER.log(Level.INFO, "Received from client: {}%n", examplePacket);

        // Send response
        session.send(new ExamplePacket("This is a response", new String[]{"This", "is", "a", "response"}));

        // Returns whether the session is active
        LOGGER.log(Level.INFO, "%nIs session active?: {}%n", session.isConnected());

        // Returns the local or remote address, depending on if it's the server or the client
        LOGGER.log(Level.INFO, "Local server address: {}%n", session.getAddress());

        // TODO: See if it can be checked whether the session is already closed and return a boolean for that
        // Closes the session, this does not stop the server. It just closes the channel!
        session.close();
        LOGGER.log(Level.INFO, OUTPUT_FOOTER);

        // Check again if session is active
        LOGGER.log(Level.INFO, "Is session active?: {}%n", session.isConnected());
    }

    @SuppressWarnings("unused") // Methods annotated with @PacketHandler are called at runtime by Hydra
    @PacketHandler
    public void onSampleSerializationPacket(ExampleSerializationPacket exampleSerializationPacket, Session session) {
        LOGGER.log(Level.INFO, OUTPUT_HEADER);

        LOGGER.log(Level.INFO, "Received from client: {}%n", exampleSerializationPacket);

        session.close();
        LOGGER.log(Level.INFO, OUTPUT_FOOTER);
    }

    @SuppressWarnings("unused") // Methods annotated with @PacketHandler are called at runtime by Hydra
    @PacketHandler
    public void onStandardPacket(StandardPacket standardPacket, Session session) {
        LOGGER.log(Level.INFO, OUTPUT_HEADER);

        LOGGER.log(Level.INFO, "Received from client using the StandardPacket: {}%n", standardPacket);

        session.close();
        LOGGER.log(Level.INFO, OUTPUT_FOOTER);
    }
}