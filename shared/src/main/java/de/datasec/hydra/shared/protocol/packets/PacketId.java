package de.datasec.hydra.shared.protocol.packets;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with love by DataSec on 29.09.2017.
 *
 * The PacketId annotation is used to mark packets with an id in the protocol of Hydra.
 * By giving the packet an id, it is possible to distinguish the packets when serializing and deserializing them.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PacketId {

    byte value() default 0;
}
