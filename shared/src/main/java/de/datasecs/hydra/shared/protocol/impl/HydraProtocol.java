package de.datasecs.hydra.shared.protocol.impl;

import de.datasecs.hydra.shared.handler.Session;
import de.datasecs.hydra.shared.handler.listener.HydraSessionListener;
import de.datasecs.hydra.shared.protocol.Protocol;
import de.datasecs.hydra.shared.protocol.packets.Packet;
import de.datasecs.hydra.shared.protocol.packets.PacketId;
import de.datasecs.hydra.shared.protocol.packets.StandardPacket;
import de.datasecs.hydra.shared.protocol.packets.listener.HydraPacketListener;
import de.datasecs.hydra.shared.protocol.packets.listener.PacketHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with love by DataSecs on 29.09.2017.
 */
public class HydraProtocol implements Protocol {

    private Map<Byte, Class<? extends Packet>> packets = new HashMap<>();

    private Map<Class<? extends Packet>, Byte> packetBytes = new HashMap<>();

    private Map<Class<?>, Method> packetListenerMethods = new HashMap<>();

    private Set<Session> sessions = new HashSet<>();

    private Session clientSession;

    private HydraPacketListener packetListener;

    private HydraSessionListener sessionListener;

    public HydraProtocol() {
        // Register StandardPacket to be ready out of the box
        registerPacket(StandardPacket.class);
    }

    @Override
    public Packet createPacket(byte id) {
        try {
            return packets.get(id).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            System.err.printf("Packet %s.class might hasn't got an empty constructor!%n\n", packets.get(id).getSimpleName());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public byte getPacketId(Packet packet) {
        return packetBytes.get(packet.getClass());
    }

    @Override
    public void registerPacket(Class<? extends Packet> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz can't be null!");
        }

        PacketId packetId = clazz.getAnnotation(PacketId.class);

        if (packetId == null) {
            throw new NullPointerException(String.format("Annotation of packet %s.class not found. Annotation might not be present!", clazz.getSimpleName()));
        }

        byte id = packetId.value();

        if (packets.containsKey(id)) {
            throw new IllegalArgumentException(String.format("Packet with id %s is already registered!", id));
        }

        packets.put(id, clazz);
        packetBytes.put(clazz, id);
    }

    @Override
    public void registerListener(HydraPacketListener packetListener) {
        if (packetListener == null) {
            throw new IllegalArgumentException("packetListener can't be null!");
        }

        this.packetListener = packetListener;

        for (Method method : packetListener.getClass().getMethods()) {
            if (method.isAnnotationPresent(PacketHandler.class)) {
                if (method.getParameterCount() == 2) {
                    Class clazz = method.getParameterTypes()[0];
                    if (Packet.class.isAssignableFrom(clazz)) {
                        if (!packetListenerMethods.containsKey(clazz)) {
                            packetListenerMethods.put(clazz, method);
                        } else {
                            throw new IllegalArgumentException(String.format("It's not possible to assign multiple PacketHandler methods for packet %s.class", clazz.getSimpleName()));
                        }
                    } else {
                        throw new IllegalArgumentException(String.format("%s is not a deriving class of Packet.class. Make sure the first argument is a deriving class of Packet.class. The first argument of the PacketHandler method is the packet it is supposed to handle!", clazz.getSimpleName()));
                    }
                } else {
                    throw new IllegalArgumentException("There are just 2 arguments allowed for a PacketHandler method!");
                }
            }
        }
    }

    @Override
    public void callPacketListener(Packet packet, Session session) {
        try {
            packetListenerMethods.get(packet.getClass()).invoke(packetListener, packet.getClass().cast(packet), session);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addSessionListener(HydraSessionListener sessionListener) {
        this.sessionListener = sessionListener;
    }

    @Override
    public void callSessionListener(boolean connected, Session session) {
        if (connected) {
            sessionListener.onConnected(session);
        } else {
            sessionListener.onDisconnected(session);
        }
    }

    @Override
    public void setClientSession(Session clientSession) {
        this.clientSession = clientSession;
    }

    @Override
    public Session getClientSession() {
        return clientSession;
    }

    @Override
    public void addSession(Session session) {
        sessions.add(session);
    }

    @Override
    public void removeSession(Session session) {
        sessions.remove(session);
    }

    @Override
    public Set<Session> getSessions() {
        return sessions;
    }

    @Override
    public HydraSessionListener getSessionListener() {
        return sessionListener;
    }

    @Override
    public HydraPacketListener getPacketListener() {
        return packetListener;
    }
}