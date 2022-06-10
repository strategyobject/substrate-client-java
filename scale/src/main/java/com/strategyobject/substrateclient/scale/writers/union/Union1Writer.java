package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.common.types.union.Union1;

public class Union1Writer extends BaseUnionWriter<Union1<?>> {
    public Union1Writer() {
        super(1, Union1::getItem0);
    }
}
