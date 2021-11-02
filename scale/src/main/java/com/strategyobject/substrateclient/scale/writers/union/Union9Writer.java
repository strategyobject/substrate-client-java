package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.types.union.Union9;

public class Union9Writer extends BaseUnionWriter<Union9<?, ?, ?, ?, ?, ?, ?, ?, ?>> {
    public Union9Writer() {
        super(9,
                Union9::getItem0,
                Union9::getItem1,
                Union9::getItem2,
                Union9::getItem3,
                Union9::getItem4,
                Union9::getItem5,
                Union9::getItem6,
                Union9::getItem7,
                Union9::getItem8);
    }
}
