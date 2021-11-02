package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.types.union.Union2;

public class Union2Writer extends BaseUnionWriter<Union2<?, ?>> {
    public Union2Writer() {
        super(2, Union2::getItem0, Union2::getItem1);
    }
}
