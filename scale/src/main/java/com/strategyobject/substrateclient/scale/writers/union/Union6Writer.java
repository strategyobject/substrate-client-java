package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.types.union.Union6;

public class Union6Writer extends BaseUnionWriter<Union6<?, ?, ?, ?, ?, ?>> {
    public Union6Writer() {
        super(6,
                Union6::getItem0,
                Union6::getItem1,
                Union6::getItem2,
                Union6::getItem3,
                Union6::getItem4,
                Union6::getItem5);
    }
}
