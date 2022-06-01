package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.common.types.union.Union8;

public class Union8Writer extends BaseUnionWriter<Union8<?, ?, ?, ?, ?, ?, ?, ?>> {
    public Union8Writer() {
        super(8,
                Union8::getItem0,
                Union8::getItem1,
                Union8::getItem2,
                Union8::getItem3,
                Union8::getItem4,
                Union8::getItem5,
                Union8::getItem6,
                Union8::getItem7);
    }
}
