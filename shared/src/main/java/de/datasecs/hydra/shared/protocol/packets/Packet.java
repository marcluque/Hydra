package de.datasecs.hydra.shared.protocol.packets;

import de.datasecs.hydra.shared.serialization.IgnoreSerialization;
import io.netty.buffer.ByteBuf;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with love by DataSecs on 29.09.2017.
 */
public abstract class Packet {

    private ByteBuf byteBuf;

    private Object objectToSerialize;

    private Map<Object, Object> fieldsToSerialize = new ConcurrentHashMap<>();

    public void setByteBuf(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    public abstract void read();

    public abstract void write();

    protected void writeByte(byte byt) {
        byteBuf.writeByte(byt);
    }

    protected byte readByte() {
        return byteBuf.readByte();
    }

    protected void writeInt(int integer) {
        byteBuf.writeInt(integer);
    }

    protected int readInt() {
        return byteBuf.readInt();
    }

    protected void writeFloat(float floatNumber) {
        byteBuf.writeFloat(floatNumber);
    }

    protected float readFloat() {
        return byteBuf.readFloat();
    }

    protected void writeDouble(double doubleNumber) {
        byteBuf.writeDouble(doubleNumber);
    }

    protected double readDouble() {
        return byteBuf.readDouble();
    }

    protected void writeLong(long longNumber) {
        byteBuf.writeLong(longNumber);
    }

    protected long readLong() {
        return byteBuf.readLong();
    }

    protected void writeString(String string) {
        byteBuf.writeInt(string.length());

        try {
            byteBuf.writeBytes(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    protected String readString() {
        byte[] bytes = new byte[byteBuf.readInt()];
        byteBuf.readBytes(bytes);

        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected <T> void writeObject(T object) {
        if (object == null) {
            throw new IllegalArgumentException("object can't be null");
        }

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(object);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            byteBuf.writeInt(bytes.length);
            byteBuf.writeBytes(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Object readObject() {
        int length = byteBuf.readInt();
        if (length > byteBuf.readableBytes()) {
            throw new IllegalStateException("length can't be larger than the readable bytes");
        }

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected <T> void writeCustomObject(T customObject, String pathOfCustomClassAtReceiver) {
        writeCustomObject(customObject, "", pathOfCustomClassAtReceiver);
    }

    private <T> void writeCustomObject(T customObject, String prefix, String pathOfCustomClassAtReceiver) {
        // Send path for rebuild of custom class
        if (!prefix.startsWith("#")) {
            writeString(String.format("%s.%s", pathOfCustomClassAtReceiver, customObject.getClass().getSimpleName()));
        }

        final boolean[] isObject = {true};
        Arrays.stream(customObject.getClass().getDeclaredFields()).filter(field -> {
            try {
                field.setAccessible(true);
                objectToSerialize = !Modifier.isTransient(field.getModifiers()) ? field.get(customObject) : null;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            return objectToSerialize != null;
        }).forEach(field -> {
            if (!(objectToSerialize instanceof Serializable)) {
                writeCustomObject(objectToSerialize, String.format("%s#", prefix), pathOfCustomClassAtReceiver);
            } else {
                String fieldName = field.getName();
                if (prefix.startsWith("#")) {
                    if (isObject[0]) {
                        fieldName = String.format("%s%s;*.%s", prefix, fieldName, customObject.getClass().getSimpleName());
                        isObject[0] = false;
                    } else {
                        fieldName = String.format("%s%s", prefix, fieldName);
                    }
                }

                System.out.println("WRITING: " + fieldName);
                writeString(fieldName);
                System.out.println("WRITING: " + objectToSerialize);
                writeObject(objectToSerialize);
            }
        });

        if (!prefix.startsWith("#")) {
            // Signalizes that transmission is over
            writeString("~");
        }
    }

    protected <T> T readCustomObject(T customObject) {
        String pathOfCustomClass = readString();
        try {
            customObject = (T) Class.forName(pathOfCustomClass).newInstance();
            pathOfCustomClass = pathOfCustomClass.substring(0, pathOfCustomClass.lastIndexOf("."));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> fields = new ConcurrentHashMap<>();
        Map<String, Object> subFields = new ConcurrentHashMap<>();

        String input;
        while (!(input = readString()).startsWith("~")) {
            if (input.contains(";")) {
                subFields.clear();

                String[] fieldNames = input.replace("*", pathOfCustomClass).split(";");

                subFields.put(fieldNames[0].replaceAll("#", ""), String.valueOf(readObject()));
                fields.put(fieldNames[1], subFields);
            } else {
                if (input.startsWith("#")) {
                    subFields.put(input.replaceAll("#", ""), readObject());
                } else {
                    fields.put(input, readObject());
                }
            }
        }

        return readCustomObject(customObject, fields);
    }

    private <T> T readCustomObject(T customObject, Map<String, Object> fields) {
        for (Method currentMethod : customObject.getClass().getDeclaredMethods()) {
            if (currentMethod.getName().contains("set") && !currentMethod.isAnnotationPresent(IgnoreSerialization.class)) {
                fields.forEach((key, value) -> {
                    Class<?> clazz = null;
                    boolean valueIsMap = value instanceof Map;

                    try {
                        if (valueIsMap) {
                            clazz = Class.forName(key);
                            key = key.substring(key.lastIndexOf(".") + 1);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    // TODO: Think of a better way than comparing the amount of characters, rather compare the class of the parameter and the class of the value
                    if ((currentMethod.getParameterCount() == 1 ? currentMethod.getParameterTypes()[0] : null) != null
                            && currentMethod.getName().toLowerCase().contains(key.toLowerCase())
                            && (currentMethod.getName().length() - 3) == key.length()) {
                        try {
                            currentMethod.invoke(customObject, valueIsMap ? readCustomObject(clazz.newInstance(), (Map<String, Object>) value) : value);
                            fields.remove(key);
                        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        return customObject;
    }

    // TODO: Custom class serialization for arrays
    protected void writeArray(Object[] array) {
        writeObject(array);
    }

    protected <T> T[] readArray() {
        int length = byteBuf.readInt();
        if (length > byteBuf.readableBytes()) {
            throw new IllegalStateException("length can't be larger than the readable bytes");
        }

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            return (T[]) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}