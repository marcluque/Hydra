package client;

import de.datasec.hydra.shared.handler.Session;
import de.datasec.hydra.shared.protocol.packets.listener.HydraPacketListener;
import de.datasec.hydra.shared.protocol.packets.listener.PacketHandler;

import java.util.Arrays;

/**
 * Created with love by DataSec on 03.11.2017.
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
        System.out.printf("Received from server: %s + %s%n", samplePacket.getSampleObject(), Arrays.toString(samplePacket.getSampleStringArray()));

        // Send response
        //session.send(new SamplePacket("This is a response", new String[]{"This", "is", "a", "response"}));

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