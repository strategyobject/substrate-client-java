package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.types.union.Union12;

public class Union12Writer extends BaseUnionWriter<Union12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> {
    public Union12Writer() {
        super(12,
                Union12::getItem0,
                Union12::getItem1,
                Union12::getItem2,
                Union12::getItem3,
                Union12::getItem4,
                Union12::getItem5,
                Union12::getItem6,
                Union12::getItem7,
                Union12::getItem8,
                Union12::getItem9,
                Union12::getItem10,
                Union12::getItem11);
    }
}
