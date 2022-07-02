package com.marcluque.hydra.example.server;

import com.marcluque.hydra.example.shared.ExamplePacket;
import com.marcluque.hydra.example.shared.serialization.ExampleSerializationPacket;
import com.marcluque.hydra.shared.handler.Session;
import com.marcluque.hydra.shared.protocol.packets.StandardPacket;
import com.marcluque.hydra.shared.protocol.packets.listener.HydraPacketListener;
import com.marcluque.hydra.shared.protocol.packets.listener.PacketHandler;

/**
 * Created with love by marcluque on 03.11.2017.
 */
public class ExampleServerPacketListener implements HydraPacketListener {

    public ExampleServerPacketListener() {
        // Do something
    }

    /* Use the @PacketHandler annotation on methods that are supposed to handle packets.
     * The amount of parameters has always to be equal 2 and in the given order. (Packet class and then the session)
     */
    @PacketHandler
    public void onSamplePacket(ExamplePacket examplePacket, Session session) {
        System.out.println("\n---PACKET-LISTENER OUTPUT---");

        // Process received packet
        System.out.printf("Received from client: %s%n", examplePacket);

        // Send response
        session.send(new ExamplePacket("This is a response", new String[]{"This", "is", "a", "response"}));

        // Returns whether the session is active
        System.out.println("\nIs session active?: " + session.isConnected());

        // Returns the local or remote address, depending if it's the server or the client
        System.out.println("Local server address: " + session.getAddress());

        // TODO: See if it can be checked whether the session is already closed and return a boolean for that
        // Closes the session, this does not stop the server. It just closes the channel!
        session.close();
        System.out.println("\nSession closed!");

        // Check again if session is active
        System.out.println("Is session active?: " + session.isConnected());
    }

    @PacketHandler
    public void onSampleSerializationPacket(ExampleSerializationPacket exampleSerializationPacket, Session session) {
        System.out.println("\n---PACKET-LISTENER OUTPUT---");

        System.out.printf("Received from client: %s%n", exampleSerializationPacket);

        session.close();
        System.out.println("\nSession closed!");
    }

    @PacketHandler
    public void onStandardPacket(StandardPacket standardPacket, Session session) {
        System.out.println("\n---PACKET-LISTENER OUTPUT---");

        System.out.printf("Received from client using the StandardPacket: %s%n", standardPacket);

        session.close();
        System.out.println("\nSession closed!");
    }
}