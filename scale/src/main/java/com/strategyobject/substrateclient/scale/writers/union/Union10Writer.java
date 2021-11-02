package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.types.union.Union10;

public class Union10Writer extends BaseUnionWriter<Union10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> {
    public Union10Writer() {
        super(10,
                Union10::getItem0,
                Union10::getItem1,
                Union10::getItem2,
                Union10::getItem3,
                Union10::getItem4,
                Union10::getItem5,
                Union10::getItem6,
                Union10::getItem7,
                Union10::getItem8,
                Union10::getItem9);
    }
}
