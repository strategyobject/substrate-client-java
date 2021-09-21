package com.strategyobject.substrateclient.scale;

import com.strategyobject.substrateclient.scale.annotations.Scale;
import com.strategyobject.substrateclient.scale.annotations.ScaleWriter;

@ScaleWriter
public class GenericScaleSelfWritable<
        T1 extends ScaleSelfWritable<T1>,
        T2 extends ScaleSelfWritable<T2>,
        T3 extends ScaleSelfWritable<T3>> implements ScaleSelfWritable<GenericScaleSelfWritable<T1, T2, T3>> {

    @Scale
    public T1 testGeneric1;

    @Scale
    public T2 testGeneric2;

    @Scale
    public T3 testGeneric3;
}
