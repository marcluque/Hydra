package server.serialization;

import java.util.Arrays;
import java.util.List;

/**
 * Created with love by DataSec on 11.02.18
 */
public class CustomClass {

    private String testString;

    // This field will be ignored when the class is serialized due to the 'transient keyword'
    private transient int testInt;

    private String[] testStringArray;

    private List<String> testStringList;

    private Object testObject;

    private CustomClassExtended customClassExtended;

    public CustomClass() {
        // This constructor is obligatory!
        // It is necessary to rebuild (deserialize) the class after the serialization
    }

    // This constructor is not obligatory. Every other constructor than the empty one is optional
    public CustomClass(String testString, int testInt, String[] testStringArray, List<String> testStringList, Object testObject, CustomClassExtended customClassExtended) {
        this.testString = testString;
        this.testInt = testInt;
        this.testStringArray = testStringArray;
        this.testStringList = testStringList;
        this.testObject = testObject;
        this.customClassExtended = customClassExtended;
    }

    /*
     * Setters for every field that is supposed to be serialized are obligatory!!
     *
     * The setter method are recognized by their pattern:
     * 1. They should contain the keyword 'set'
     * 2. Their method names can contain the class name of the field
     * One of these 2 should be fulfilled
     * &&
     * 3. They should have the same type as parameter as the field
     * 4. The parameter can have a super class of the field class for polymorphic purposes
     * Also one of these 2 should be fulfilled
     */
    public void setTestString(String testString) {
        this.testString = testString;
    }

    // This setter method is not obligatory as the field testInt is marked with the keyword transient
    public void setTestInt(int testInt) {
        this.testInt = testInt;
    }

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

    // Auto-generated toString method by IntelliJ for example purposes
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