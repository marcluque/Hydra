package de.datasec.hydra.shared.protocol.packets;

/**
 * Created by DataSec on 29.09.2017.
 */
public interface PacketListener {

    void onPacket(Packet packet);
}
