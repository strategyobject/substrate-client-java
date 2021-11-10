package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.types.union.Union4;

public class Union4Writer extends BaseUnionWriter<Union4<?, ?, ?, ?>> {
    public Union4Writer() {
        super(4,
                Union4::getItem0,
                Union4::getItem1,
                Union4::getItem2,
                Union4::getItem3);
    }
}
