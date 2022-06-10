package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.common.types.union.Union7;

public class Union7Writer extends BaseUnionWriter<Union7<?, ?, ?, ?, ?, ?, ?>> {
    public Union7Writer() {
        super(7,
                Union7::getItem0,
                Union7::getItem1,
                Union7::getItem2,
                Union7::getItem3,
                Union7::getItem4,
                Union7::getItem5,
                Union7::getItem6);
    }
}
