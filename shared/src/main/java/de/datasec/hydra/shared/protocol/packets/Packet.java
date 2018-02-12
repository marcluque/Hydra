package de.datasec.hydra.shared.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with love by DataSec on 29.09.2017.
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
        String packageName = customObject.getClass().getName();

        serializeClass(customObject, fieldsToSerialize);

        writeString(String.format("%s;%s", pathOfCustomClassAtReceiver, packageName.substring(packageName.lastIndexOf(".") + 1)));
        writeObject(fieldsToSerialize);
    }

    private <T> void serializeClass(T customClass, Map<Object, Object> objects) {
        Arrays.stream(customClass.getClass().getDeclaredFields()).filter(field -> {
            try {
                field.setAccessible(true);
                if (!Modifier.isTransient(field.getModifiers())) {
                    objectToSerialize = field.get(customClass);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            return objectToSerialize != null;
        }).forEach(field -> {
            if (!(objectToSerialize instanceof Serializable)) {
                // Add a '#' to the elements that are classes that need to be serialized
                Map<Object, Object> subFields = new ConcurrentHashMap<>();
                Object originalObject = objectToSerialize;
                serializeClass(objectToSerialize, subFields);
                String originalObjectClass = originalObject.getClass().getName();
                objects.put("#" + originalObjectClass.substring(originalObjectClass.lastIndexOf(".") + 1), subFields);
            } else {
                objects.put(objectToSerialize.getClass(), objectToSerialize);
            }
        });
    }

    protected <T> T readCustomObject(T customObject) {
        String[] packagesNames = readString().split(";");

        try {
            Class<?> customObjectClass = Class.forName(String.format("%s.%s", packagesNames[0], packagesNames[1]));
            System.out.println("RECEIVED CLASS: " + customObjectClass);

            fieldsToSerialize = (Map<Object, Object>) readObject();
            System.out.println("RECEIVED MAP: " + fieldsToSerialize);
            // Replace the keys that have a normal class name and are marked with a '#', with the appropriate path
            fieldsToSerialize.forEach((key, value) -> {
                if (key instanceof String && ((String) key).startsWith("#")) {
                    String className = ((String) key).substring(1);
                    fieldsToSerialize.remove(key);
                    try {
                        fieldsToSerialize.put(Class.forName(String.format("%s.%s", packagesNames[0], className)), value);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

            customObject = deserializeClass(customObject = (T) customObjectClass.newInstance(), fieldsToSerialize);

            // Clear everything for next use
            fieldsToSerialize.clear();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return customObject;
    }

    private <T> T deserializeClass(T customObject, Map<Object, Object> fields) {
        Arrays.stream(customObject.getClass().getDeclaredMethods()).filter(method -> method.getName().contains("set")).forEach(currentMethod -> fields.forEach((key, value) -> {
            Class<?> tempClazz = null;
            try {
                tempClazz = Class.forName(key.toString().split(" ")[1]);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Class<?> clazz = tempClazz;

            /* The ifs check whether (the class of the key is a super class (includes case that they are the same and the actual case the other way around)
             * of the class of the parameter in the setter || the class of the key contains the class of the parameter in the setter)
             * && (the method name contains set || the method name contains the name of the class of the key)
             */
            Class<?> methodParameterClazz = currentMethod.getParameterCount() == 1 ? currentMethod.getParameterTypes()[0] : null;
            if (methodParameterClazz != null) {
                if (((clazz.isAssignableFrom(methodParameterClazz) || methodParameterClazz.isAssignableFrom(clazz)) || clazz.toString().contains(methodParameterClazz.toString()))
                        && (currentMethod.getName().contains(clazz.getSimpleName()) || currentMethod.getName().contains("set"))) {
                    try {
                        currentMethod.invoke(customObject, value instanceof Map ? deserializeClass(clazz.newInstance(), (Map<Object, Object>) value) : value);
                        fields.remove(key);
                    } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }));

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