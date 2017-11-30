package de.datasec.hydra.shared.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.*;

/**
 * Created with love by DataSec on 29.09.2017.
 */
public abstract class Packet {

    private ByteBuf byteBuf;

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

    protected void writeString(String string) throws UnsupportedEncodingException {
        byteBuf.writeInt(string.length());
        byteBuf.writeBytes(string.getBytes("UTF-8"));
    }

    protected String readString() throws UnsupportedEncodingException {
        byte[] bytes = new byte[byteBuf.readInt()];
        byteBuf.readBytes(bytes);

        return new String(bytes, "UTF-8");
    }

    protected void writeObject(Object object) throws IOException {
        if (object == null) {
            throw new IllegalArgumentException("object cannot be null");
        }

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(object);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            byteBuf.writeInt(bytes.length);
            byteBuf.writeBytes(bytes);
        }
    }

    protected Object readObject() throws IOException, ClassNotFoundException {
        Object object;

        int length = byteBuf.readInt();
        if (length > byteBuf.readableBytes()) {
            throw new IllegalStateException("length cannot be larger than the readable bytes");
        }

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes); ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            object = objectInputStream.readObject();
        }

        return object;
    }
}