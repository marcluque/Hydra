package de.datasecs.hydra.example.client;

import de.datasecs.hydra.example.shared.ExamplePacket;
import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.protocol.packets.listener.HydraPacketListener;
import de.datasecs.hydra.shared.protocol.packets.listener.PacketHandler;

/**
 * Created with love by DataSecs on 03.11.2017.
 */
public class ExampleClientPacketListener implements HydraPacketListener {

    public ExampleClientPacketListener() {
        // Do something
    }

    /* Use the @PacketHandler annotation on methods that are supposed to handle packets.
     * The amount of parameters has always to be equal 2 and in the given order. (Packet class and then the session)
     */
    @PacketHandler
    public void onSamplePacket(ExamplePacket examplePacket, Session session) {
        System.out.println("\n---PACKET-LISTENER OUTPUT---");

        // Process received packet
        System.out.printf("Received from server: %s%n", examplePacket);

        // Send response
        //session.send(new ExamplePacket("This is a response", new String[]{"This", "is", "a", "response"}));

        // Returns if the session is active
        System.out.println("\nIs session active?: " + session.isConnected());

        // Returns the local or remote address, depending if it's the server or the client
        System.out.println("Remote address (client is connected to): " + session.getAddress());

        // Closes the session, this does not stop the client. It just closes the channel!
        session.close();
        System.out.println("\nSession closed!");

        // Check again if session is active
        System.out.println("Is session active?: " + session.isConnected());
    }
}