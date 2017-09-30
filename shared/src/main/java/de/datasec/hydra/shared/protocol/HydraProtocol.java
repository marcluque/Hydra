package de.datasec.hydra.shared.protocol;

import de.datasec.hydra.shared.handler.HydraSession;
import de.datasec.hydra.shared.protocol.packets.Packet;
import de.datasec.hydra.shared.protocol.packets.PacketId;
import de.datasec.hydra.shared.protocol.packets.PacketListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DataSec on 29.09.2017.
 */
public class HydraProtocol {

    private Map<Byte, Class<? extends Packet>> packets = new HashMap<>();

    private Map<Class<? extends Packet>, Byte> packetBytes = new HashMap<>();

    private PacketListener packetListener = null;

    private HydraSession session;

    public void registerPacket(Class<? extends Packet> clazz) {
        if(clazz == null) {
            throw new IllegalArgumentException("clazz can't be null.");
        }

        byte id = clazz.getAnnotation(PacketId.class).value();

        if (packets.containsKey(id)) {
            throw new IllegalArgumentException("Packet with id " + id + " is already registered!");
        }

        packets.put(id, clazz);
        packetBytes.put(clazz, id);
    }

    public Packet createPacket(byte id) {
        try {
            return packets.get(id).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public byte getPacketId(Packet packet) {
        return packetBytes.get(packet.getClass());
    }

    public void registerListener(PacketListener packetListener) {
        if (packetListener == null) {
            throw new IllegalArgumentException("packetListener can't be null.");
        }

        this.packetListener = packetListener;
    }

    public void callListener(Packet packet) {
        if (packetListener == null) {
            throw new NullPointerException("packetListener can't be null. Might not be registered in protocol.");
        }

        packetListener.onPacket(packet, session);
    }

    public void setSession(HydraSession session) {
        this.session = session;
    }
}
