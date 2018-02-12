package server.serialization;

import de.datasec.hydra.shared.serialization.IgnoreSerialization;

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

    /* Setters for every field that is supposed to be serialized are obligatory!!
     *
     * The setter method are recognized by the following pattern:
     * 1. They have to contain the keyword 'set'
     * 2. They have to contain the name of the field that is serialized
     * This unfortunately is currently necessary because the length of the methods is checked to determine the correct one
     *
     * HINT: Do it the same way as in this example. These setter methods are just auto-generated which are guaranteed to work
     */
    public void setTestString(String testString) {
        this.testString = testString;
    }

    /* This setter method is not obligatory (actually unnecessary and therefore marked with the
     * 'ignoreSerialization' annotation) as the field testInt is marked with the keyword transient
     * NOT EVERY METHOD NEEDS THIS ANNOTATION:
     * This method just needs the annotation becase it contains the 'keyword set'. That's a keyword the serializer
     * looks for. Therefore it needs to be marked with this annotation.
     */
    @IgnoreSerialization
    public void setTestInt(int testInt) {
        this.testInt = testInt;
    }

    // This doesn't need an annotation as it hasn't got the keyword 'set'
    public void testInt(int testInt) {
        // Do something with testInt
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