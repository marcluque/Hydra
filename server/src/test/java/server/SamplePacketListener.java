package server;

import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.protocol.packets.listener.HydraPacketListener;
import de.datasecs.hydra.shared.protocol.packets.listener.PacketHandler;
import server.packets.SamplePacket;
import server.packets.SampleSerializationPacket;

/**
 * Created with love by DataSecs on 03.11.2017.
 */
public class SamplePacketListener implements HydraPacketListener {

    public SamplePacketListener() {
        // Do something
    }

    /* Use the @PacketHandler annotation on methods that are supposed to handle packets.
     * The amount of parameters has always to be equal 2 and in the given order. (Packet class and then the session)
     */
    @PacketHandler
    public void onSamplePacket(SamplePacket samplePacket, Session session) {
        System.out.println("\n---PACKET-LISTENER OUTPUT---");

        // Process received packet
        System.out.printf("Received from client: %s%n", samplePacket);

        // Send response
        session.send(new SamplePacket("This is a response", new String[]{"This", "is", "a", "response"}));

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
    public void onSampleSerializationPacket(SampleSerializationPacket sampleSerializationPacket, Session session) {
        System.out.println("\n---PACKET-LISTENER OUTPUT---");

        System.out.printf("Received from client: %s%n", sampleSerializationPacket);

        session.close();
        System.out.println("\nSession closed!");
    }
}