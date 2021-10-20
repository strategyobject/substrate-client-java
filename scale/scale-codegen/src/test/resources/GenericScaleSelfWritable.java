package com.strategyobject.substrateclient.scale;

import com.strategyobject.substrateclient.scale.annotations.Scale;
import com.strategyobject.substrateclient.scale.annotations.ScaleWriter;

@ScaleWriter
public class GenericScaleSelfWritable<
        T1 extends ScaleSelfWritable<T1>,
        T2 extends ScaleSelfWritable<T2>,
        T3 extends ScaleSelfWritable<T3>> implements ScaleSelfWritable<GenericScaleSelfWritable<T1, T2, T3>> {

    @Scale
    private T1 testGeneric1;

    @Scale
    private T2 testGeneric2;

    @Scale
    private T3 testGeneric3;

    public T1 getTestGeneric1() {
        return testGeneric1;
    }

    public void setTestGeneric1(T1 testGeneric1) {
        this.testGeneric1 = testGeneric1;
    }

    public T2 getTestGeneric2() {
        return testGeneric2;
    }

    public void setTestGeneric2(T2 testGeneric2) {
        this.testGeneric2 = testGeneric2;
    }

    public T3 getTestGeneric3() {
        return testGeneric3;
    }

    public void setTestGeneric3(T3 testGeneric3) {
        this.testGeneric3 = testGeneric3;
    }
}
