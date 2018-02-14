package de.datasec.hydra.shared.serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with love by DataSec on 29.09.2017.
 *
 * The IgnoreSerialization annotation is specifically intended and used for setter methods in a custom class.
 * The annotation acts like the keyword 'transient' for fields in classes that implement the serializable class.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreSerialization {

}