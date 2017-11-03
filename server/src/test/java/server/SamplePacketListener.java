package server;

import de.datasec.hydra.shared.handler.Session;
import de.datasec.hydra.shared.protocol.packets.HydraPacketListener;
import de.datasec.hydra.shared.protocol.packets.PacketHandler;

/**
 * Created by DataSec on 03.11.2017.
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
        // Process received packet
        System.out.printf("Received from client: %s%n", samplePacket.getSampleObject());

        // Send response
        session.send(new SamplePacket("This is a response"));
    }
}