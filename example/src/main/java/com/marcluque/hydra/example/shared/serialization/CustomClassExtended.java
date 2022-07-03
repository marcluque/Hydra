package com.marcluque.hydra.example.shared.serialization;

import java.util.UUID;

/**
 * Created with love by marcluque on 12.02.18
 */
public class CustomClassExtended {

    private String testStringExtended;

    private UUID testUUID;

    private CustomClassExtended customClassExtendedObject;

    private long testLong;

    private Class<?> testClass;

    @SuppressWarnings("unused")
    public CustomClassExtended() {
        // This constructor is obligatory!
        // It is necessary to rebuild (deserialize) the class after the serialization
    }

    // Optional constructor
    public CustomClassExtended(String testStringExtended, UUID testUUID, CustomClassExtended customClassExtendedObject, long testLong, Class<?> testClass) {
        this.testStringExtended = testStringExtended;
        this.testUUID = testUUID;
        this.customClassExtendedObject = customClassExtendedObject;
        this.testLong = testLong;
        this.testClass = testClass;
    }

    // Auto-generated toString method by IntelliJ for example purposes
    @Override
    public String toString() {
        return "CustomClassExtended {" + "%n" +
                "testStringExtended='" + testStringExtended + '\'' + ",%n" +
                "testUUID=" + testUUID + ",%n" +
                "customClassExtended=" + customClassExtendedObject + ",%n" +
                "testLong=" + testLong + ",%n" +
                "testClass=" + testClass + "%n" +
                "}";
    }
}