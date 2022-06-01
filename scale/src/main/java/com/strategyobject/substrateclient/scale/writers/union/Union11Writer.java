package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.common.types.union.Union11;

public class Union11Writer extends BaseUnionWriter<Union11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> {
    public Union11Writer() {
        super(11,
                Union11::getItem0,
                Union11::getItem1,
                Union11::getItem2,
                Union11::getItem3,
                Union11::getItem4,
                Union11::getItem5,
                Union11::getItem6,
                Union11::getItem7,
                Union11::getItem8,
                Union11::getItem9,
                Union11::getItem10);
    }
}
