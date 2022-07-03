package com.marcluque.hydra.shared.protocol.packets;

import io.netty.buffer.ByteBuf;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;

/**
 * Created with love by marcluque on 29.09.2017.
 */
public abstract class Packet {

    private static final Logger LOGGER = LogManager.getLogger(Packet.class.getName());

    private Object objectToSerialize;

    public abstract void read(ByteBuf byteBuf);

    public abstract void write(ByteBuf byteBuf);

    protected void writeInt(ByteBuf byteBuf, int integer) {
        byteBuf.writeInt(integer);
    }

    protected int readInt(ByteBuf byteBuf) {
        return byteBuf.readInt();
    }

    protected void writeString(ByteBuf byteBuf, String string) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }

    protected String readString(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readInt()];
        byteBuf.readBytes(bytes);

        return new String(bytes, StandardCharsets.UTF_8);
    }

    // TODO: Add serialization for custom objects that are send using .send(customObject) or whenever this method is called
    protected void writeObject(ByteBuf byteBuf, Object object) {
        if (object == null) {
            throw new IllegalArgumentException("object cannot be null");
        }

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(object);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            byteBuf.writeInt(bytes.length);
            byteBuf.writeBytes(bytes);
        } catch (IOException e) {
            LOGGER.log(Level.WARN, e);
        }
    }

    protected Object readObject(ByteBuf byteBuf) {
        Object object = null;

        int length = byteBuf.readInt();
        if (length > byteBuf.readableBytes()) {
            throw new IllegalStateException("length cannot be larger than the readable bytes");
        }

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            object = objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.WARN, e);
        }

        return object;
    }

    protected <T> void writeCustomObject(ByteBuf byteBuf, T customObject) {
        // Send path for rebuild of custom class
        writeString(byteBuf, customObject.getClass().getName());

        // Serialize objects and send them as String(fieldName) + Object(value)
        writeCustomObject(byteBuf, customObject, "");

        // Signalizes that transmission is over
        writeString(byteBuf, "~");
    }

    private <T> void writeCustomObject(ByteBuf byteBuf, T customObject, String prefix) {
        Field[] declaredFields = customObject.getClass().getDeclaredFields();

        for (int i = 0; i < declaredFields.length - 1; i++) {
            serializeField(byteBuf, declaredFields[i], prefix, customObject);
        }

        prefix = prefix.startsWith("#") ? "§" + prefix : prefix;
        serializeField(byteBuf, declaredFields[declaredFields.length - 1], prefix, customObject);
    }

    private <T> void serializeField(ByteBuf byteBuf, Field declaredField, String prefix, T customObject) {
        if (!Modifier.isTransient(declaredField.getModifiers())) {
            boolean isAccessible = declaredField.canAccess(customObject);
            declaredField.setAccessible(true);
            try {
                objectToSerialize = declaredField.get(customObject);
            } catch (IllegalAccessException e) {
                LOGGER.log(Level.WARN, e);
            }

            // Ignore if object is null
            if (objectToSerialize == null) {
                return;
            }

            if (!(objectToSerialize instanceof Serializable)) {
                writeString(byteBuf, String.format("$%s#%s;%s", prefix, objectToSerialize.getClass().getCanonicalName(), declaredField.getName()));
                writeCustomObject(byteBuf, objectToSerialize, String.format("%s#", prefix));
            } else {
                writeString(byteBuf, String.format("%s%s", prefix, declaredField.getName()));
                writeObject(byteBuf, objectToSerialize);
            }
            declaredField.setAccessible(isAccessible);
        }
    }

    protected <T> T readCustomObject(ByteBuf byteBuf) {
        T customObject = null;
        try {
            // We let the user of the function determine the correct type
            //noinspection unchecked
            customObject = (T) Class.forName(readString(byteBuf)).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            LOGGER.log(Level.WARN, e);
        }

        return readCustomObject(byteBuf, customObject);
    }

    protected <T> T readCustomObject(ByteBuf byteBuf, T object) {
        String input;
        while (!(input = readString(byteBuf)).startsWith("~")) {
            // $ indicates the beginning of an embedded class
            if (input.startsWith("$")) {
                input = input.replace("#", "");
                T subObject = getNewInstance(input.substring(1, input.lastIndexOf(";")));
                subObject = readCustomObject(byteBuf, subObject);
                setField(object, input.substring(input.lastIndexOf(";") + 1), subObject);
            }
            // § indicates the last object of an embedded class. Read it and then return object because it's complete
            else if (input.startsWith("§")) {
                setField(object, input.substring(1).replace("#", ""), readObject(byteBuf));
                return object;
            } else {
                if (input.startsWith("#")) {
                    input = input.replace("#", "");
                }

                setField(object, input, readObject(byteBuf));
            }
        }

        return object;
    }

    private <T> T getNewInstance(String path) {
        try {
            // We let the user of the function determine the correct type
            //noinspection unchecked
            return (T) Class.forName(path).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            LOGGER.log(Level.WARN, e);
        }

        return null;
    }

    private <T> void setField(T customObject, String fieldName, Object fieldValue) {
        boolean isAccessible;
        Field field = null;
        try {
            field = customObject.getClass().getDeclaredField(fieldName);
            isAccessible = field.canAccess(customObject);
            field.setAccessible(true);
            field.set(customObject, fieldValue);
            field.setAccessible(isAccessible);
        } catch (Exception e) {
            LOGGER.log(Level.WARN, e);
            String additionalInformation = "\nAdditional information:\n" +
                    "customObject: " + customObject + "\n" +
                    "Field name: " + fieldName + "\n" +
                    "Field value: " + fieldValue + "\n" +
                    "Found field by name: " + field + "\n" +
                    "Field Accessibility: "
                    + (field == null ? "Could not print accessibility, field == null" : field.canAccess(customObject))
                    + "\n";
            LOGGER.log(Level.WARN, additionalInformation);
        }
    }

    protected void writeArray(ByteBuf byteBuf, Object[] array) {
        writeObject(byteBuf, array);
    }

    protected <T> T[] readArray(ByteBuf byteBuf) {
        int length = byteBuf.readInt();
        if (length > byteBuf.readableBytes()) {
            throw new IllegalStateException("length can't be larger than the readable bytes");
        }

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            // We let the user of the function determine the correct type
            //noinspection unchecked
            return (T[]) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.WARN, e);
        }

        return null;
    }

    protected <T> void writeCustomClassArray(ByteBuf byteBuf, T[] array) {
        writeInt(byteBuf, array.length);
        writeString(byteBuf, array.getClass().getCanonicalName().replace("[]", ""));
        for (T t : array) {
            writeCustomObject(byteBuf, t);
        }
    }

    protected <T> T[] readCustomClassArray(ByteBuf byteBuf) {
        int length = readInt(byteBuf);
        String path = readString(byteBuf);
        T[] array = null;
        try {
            // We let the user of the function determine the correct type
            //noinspection unchecked
            array = (T[]) Array.newInstance(Class.forName(path), length);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.WARN, e);
        }

        Objects.requireNonNull(array, "Array could not be instantiated.");
        for (int i = 0; i < length; i++) {
            array[i] = readCustomObject(byteBuf);
        }

        return array;
    }

    protected <T> void writeCustomClassCollection(ByteBuf byteBuf, Collection<T> collection) {
        writeInt(byteBuf, collection.size());
        writeString(byteBuf, collection.getClass().getCanonicalName());
        collection.forEach(object -> writeCustomObject(byteBuf, object));
    }

    protected <T> Collection<T> readCustomClassCollection(ByteBuf byteBuf) {
        int length = readInt(byteBuf);

        Collection<T> collection = null;
        try {
            // We let the user of the function determine the correct type
            //noinspection unchecked
            collection = (Collection<T>) Class.forName(readString(byteBuf)).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            LOGGER.log(Level.WARN, e);
        }

        Objects.requireNonNull(collection);
        for (int i = 0; i < length; i++) {
            collection.add(readCustomObject(byteBuf));
        }

        return collection;
    }
}