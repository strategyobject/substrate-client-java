package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.common.types.union.Union5;

public class Union5Writer extends BaseUnionWriter<Union5<?, ?, ?, ?, ?>> {
    public Union5Writer() {
        super(5,
                Union5::getItem0,
                Union5::getItem1,
                Union5::getItem2,
                Union5::getItem3,
                Union5::getItem4);
    }
}
