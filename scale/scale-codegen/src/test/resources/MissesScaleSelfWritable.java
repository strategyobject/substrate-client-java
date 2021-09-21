package com.strategyobject.substrateclient.scale;

import com.strategyobject.substrateclient.scale.annotations.ScaleWriter;

@ScaleWriter
public class MissesScaleSelfWritable<T> implements ScaleSelfWritable<T> {
    public String testString;
}
