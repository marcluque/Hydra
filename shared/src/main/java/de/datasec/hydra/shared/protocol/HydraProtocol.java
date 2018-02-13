package de.datasec.hydra.shared.protocol;

import de.datasec.hydra.shared.handler.Session;
import de.datasec.hydra.shared.handler.listener.HydraSessionListener;
import de.datasec.hydra.shared.protocol.packets.Packet;
import de.datasec.hydra.shared.protocol.packets.PacketId;
import de.datasec.hydra.shared.protocol.packets.listener.HydraPacketListener;
import de.datasec.hydra.shared.protocol.packets.listener.PacketHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created with love by DataSec on 29.09.2017.
 *
 *
 */
public class HydraProtocol {

    private Map<Byte, Class<? extends Packet>> packets = new HashMap<>();

    private Map<Class<? extends Packet>, Byte> packetBytes = new HashMap<>();

    private Map<Class<?>, Method> packetListenerMethods = new HashMap<>();

    private Set<Session> sessions = new HashSet<>();

    private Session clientSession;

    private HydraPacketListener packetListener;

    private HydraSessionListener sessionListener;

    public Packet createPacket(byte id) {
        try {
            return packets.get(id).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            System.err.printf("Packet %s.class might hasn't got an empty constructor!%n\n", packets.get(id).getSimpleName());
            e.printStackTrace();
        }

        return null;
    }

    public byte getPacketId(Packet packet) {
        return packetBytes.get(packet.getClass());
    }

    /**
     *
     *
     * @param clazz
     */
    protected void registerPacket(Class<? extends Packet> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz can't be null!");
        }

        PacketId packetId = clazz.getAnnotation(PacketId.class);

        if (packetId == null) {
            throw new NullPointerException(String.format("Annotation of packet %s.class not found. Annotation might not be defined!", clazz.getSimpleName()));
        }

        byte id = packetId.value();

        if (packets.containsKey(id)) {
            throw new IllegalArgumentException(String.format("Packet with id %s is already registered!", id));
        }

        packets.put(id, clazz);
        packetBytes.put(clazz, id);
    }

    /**
     *
     *
     * @param packetListener
     */
    protected void registerListener(HydraPacketListener packetListener) {
        if (packetListener == null) {
            throw new IllegalArgumentException("packetListener can't be null!");
        }

        this.packetListener = packetListener;

        Arrays.stream(packetListener.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(PacketHandler.class))
                .forEach(method -> {
                    if (method.getParameterCount() == 2) {
                        Class clazz = method.getParameterTypes()[0];
                        if (Packet.class.isAssignableFrom(clazz)) {
                            if (!packetListenerMethods.containsKey(clazz)) {
                                packetListenerMethods.put(clazz, method);
                            } else {
                                throw new IllegalArgumentException(String.format("It's not possible to assign multiple PacketHandler methods for packet %s.class", clazz.getSimpleName()));
                            }
                        } else {
                            throw new IllegalArgumentException(String.format("%s is not a deriving class of Packet.class. Make sure the first argument is a deriving class of Packet.class. And the first argument of the PacketHandler method is the packet itself!", clazz.getSimpleName()));
                        }
                    } else {
                        throw new IllegalArgumentException("There are just 2 arguments allowed for a PacketHandler method!");
                    }
                });
    }

    public void callPacketListener(Packet packet, Session session) {
        try {
            packetListenerMethods.get(packet.getClass()).invoke(packetListener, packet.getClass().cast(packet), session);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void addSessionListener(HydraSessionListener sessionListener) {
        this.sessionListener = sessionListener;
    }

    public void callSessionListener(boolean connected, Session session) {
        if (connected) {
            sessionListener.onConnected(session);
        } else {
            sessionListener.onDisconnected(session);
        }
    }

    public void setClientSession(Session clientSession) {
        this.clientSession = clientSession;
    }

    public Session getClientSession() {
        return clientSession;
    }

    public void addSession(Session session) {
        sessions.add(session);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }

    public Set<Session> getSessions() {
        return sessions;
    }
}