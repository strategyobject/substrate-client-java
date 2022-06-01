package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.common.types.union.Union3;

public class Union3Writer extends BaseUnionWriter<Union3<?, ?, ?>> {
    public Union3Writer() {
        super(3,
                Union3::getItem0,
                Union3::getItem1,
                Union3::getItem2);
    }
}
