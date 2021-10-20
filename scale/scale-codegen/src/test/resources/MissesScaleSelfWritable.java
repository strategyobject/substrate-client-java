package com.strategyobject.substrateclient.scale;

import com.strategyobject.substrateclient.scale.annotations.ScaleWriter;

@ScaleWriter
public class MissesScaleSelfWritable<T> implements ScaleSelfWritable<T> {
    private String testString;

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }
}
