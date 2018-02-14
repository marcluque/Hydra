package de.datasec.hydra.shared.protocol.packets.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with love by DataSec on 01.10.2017.
 *
 * The PacketHandler annotation is used for methods in packet listener classes, that are supposed to handle packets.
 * Simply add the annotation to the method that is supposed to handle a specific packet.
 * But be aware of the notation the methods must have. Check the examples for more accurate information on the correct usage.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PacketHandler {

}