// IReadWrite.aidl
package com.example.shareapp.aidl;

// Declare any non-default types here with import statements

interface IReadWrite {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
    void writeData(String data);
    String readData();
}
