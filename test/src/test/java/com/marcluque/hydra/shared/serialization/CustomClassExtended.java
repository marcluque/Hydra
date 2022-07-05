package com.marcluque.hydra.shared.serialization;

import java.util.UUID;

/**
 * Created with love by marcluque on 28.03.19
 */
public class CustomClassExtended {

    private String testStringExtended;

    private UUID testUUID;

    private long testLong;

    private Class<?> testClass;

    public CustomClassExtended() {}

    public CustomClassExtended(String testStringExtended, UUID testUUID, long testLong, Class<?> testClass) {
        this.testStringExtended = testStringExtended;
        this.testUUID = testUUID;
        this.testLong = testLong;
        this.testClass = testClass;
    }

    public void setTestStringExtended(String testStringExtended) {
        this.testStringExtended = testStringExtended;
    }

    public void setTestUUID(UUID testUUID) {
        this.testUUID = testUUID;
    }

    public void setTestLong(long testLong) {
        this.testLong = testLong;
    }

    public void setTestClass(Class<?> testClass) {
        this.testClass = testClass;
    }

    public String getTestStringExtended() {
        return testStringExtended;
    }

    public UUID getTestUUID() {
        return testUUID;
    }

    public long getTestLong() {
        return testLong;
    }

    public Class<?> getTestClass() {
        return testClass;
    }

    @Override
    public String toString() {
        return "CustomClassExtended{" +
                "testStringExtended='" + testStringExtended + '\'' +
                ", testUUID=" + testUUID +
                ", testLong=" + testLong +
                ", testClass=" + testClass +
                '}';
    }
}