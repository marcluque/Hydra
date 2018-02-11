package client;

import de.datasec.hydra.client.Client;
import de.datasec.hydra.client.HydraClient;
import de.datasec.hydra.shared.handler.Session;
import de.datasec.hydra.shared.handler.listener.HydraSessionListener;
import io.netty.channel.ChannelOption;

/**
 * Created with love by DataSec on 02.11.2017.
 */
public class ExampleClient {

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
        HydraClient client = new Client.Builder("188.68.54.85", 8888, new SampleProtocol())
                .workerThreads(4)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .addSessionListener(new HydraSessionListener() {
                    @Override
                    public void onConnected(Session session) {
                        System.out.println("Connected to server!");
                    }

                    @Override
                    public void onDisconnected(Session session) {
                        System.out.println("\nDisconnected from server!");
                    }
                })
                .build();

        // Checks if the client is connected to its remote host
        if (client.isConnected()) {
            // Returns the session that was created for the client and its remote host
            session = client.getSession();
            System.out.println("\nClient is online!");
            System.out.printf("Socket address: %s%n", session.getAddress());
        }

        /* Send a packet to the server via the session the client has saved */
        // Sends a String, that is converted to a Object and an array, the type of the array is defined in SamplePacket.class
        //session.send(new SamplePacket("This is a message", new String[]{"This", "is", "a", "message"}));
        // Sends a list, that is converted to a Object and the array, like above
        //session.send(new SamplePacket(Arrays.asList("This", "is", "a", "message", "2"), new String[]{"This", "is", "a", "message", "2"}));

        // Closes the connection and releases all occupied resources
        //client.close();
    }
}