package com.marcluque.hydra.shared.serialization;

import java.util.Arrays;
import java.util.List;

/**
 * Created with love by marcluque on 28.03.19
 */
public class CustomClass {

    private String testString;

    private transient int testInt;

    private String[] testStringArray;

    private List<String> testStringList;

    private Object testObject;

    private CustomClassExtended customClassExtended;

    public CustomClass() {}

    public CustomClass(String testString, int testInt, String[] testStringArray, List<String> testStringList, Object testObject, CustomClassExtended customClassExtended) {
        this.testString = testString;
        this.testInt = testInt;
        this.testStringArray = testStringArray;
        this.testStringList = testStringList;
        this.testObject = testObject;
        this.customClassExtended = customClassExtended;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }

    @IgnoreSerialization
    public void setTestInt(int testInt) {
        this.testInt = testInt;
    }

    public void testInt(int testInt) {}

    public void setTestStringArray(String[] testStringArray) {
        this.testStringArray = testStringArray;
    }

    public void setTestStringList(List<String> testStringList) {
        this.testStringList = testStringList;
    }

    public void setTestObject(Object testObject) {
        this.testObject = testObject;
    }

    public void setCustomClassExtended(CustomClassExtended customClassExtended) {
        this.customClassExtended = customClassExtended;
    }

    public String getTestString() {
        return testString;
    }

    public int getTestInt() {
        return testInt;
    }

    public String[] getTestStringArray() {
        return testStringArray;
    }

    public List<String> getTestStringList() {
        return testStringList;
    }

    public Object getTestObject() {
        return testObject;
    }

    public CustomClassExtended getCustomClassExtended() {
        return customClassExtended;
    }

    @Override
    public String toString() {
        return "CustomClass{" +
                "testString='" + testString + '\'' +
                ", testStringArray=" + Arrays.toString(testStringArray) +
                ", testStringList=" + testStringList +
                ", testObject=" + testObject +
                ", customClassExtended=" + customClassExtended +
                '}';
    }
}