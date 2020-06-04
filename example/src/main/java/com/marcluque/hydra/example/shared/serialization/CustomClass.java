package com.marcluque.hydra.example.shared.serialization;

import com.marcluque.hydra.shared.serialization.IgnoreSerialization;

import java.util.Arrays;
import java.util.List;

/**
 * Created with love by marcluque on 11.02.18
 */
public class CustomClass {

    private String testString;

    // This field will be ignored when the class is serialized due to the 'transient keyword'
    private transient int testInt;

    private String[] testStringArray;

    private CustomClassExtended customClassExtended;

    private List<String> testStringList;

    private Object testObject;

    public CustomClass() {
        // This constructor is obligatory!
        // It is necessary to rebuild (deserialize) the class after the serialization
    }

    // This constructor is not obligatory. Every other constructor than the empty one is optional
    public CustomClass(String testString, int testInt, String[] testStringArray, CustomClassExtended customClassExtended, List<String> testStringList, Object testObject) {
        this.testString = testString;
        this.testInt = testInt;
        this.testStringArray = testStringArray;
        this.customClassExtended = customClassExtended;
        this.testStringList = testStringList;
        this.testObject = testObject;
    }

    // Auto-generated toString method by IntelliJ for example purposes
    @Override
    public String toString() {
        return "CustomClass{\n" +
                "testString='" + testString + '\'' + ",\n" +
                "testStringArray=" + Arrays.toString(testStringArray) + ",\n" +
                "testStringList=" + testStringList + ",\n" +
                "testObject=" + testObject + ",\n" +
                "customClassExtended=" + customClassExtended + "\n" +
                "}";
    }
}