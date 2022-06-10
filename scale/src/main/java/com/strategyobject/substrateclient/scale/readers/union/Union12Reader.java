package com.strategyobject.substrateclient.scale.readers.union;

import com.strategyobject.substrateclient.common.types.union.Union12;

public class Union12Reader extends BaseUnionReader<Union12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> {
    public Union12Reader() {
        super(12,
                Union12::withItem0,
                Union12::withItem1,
                Union12::withItem2,
                Union12::withItem3,
                Union12::withItem4,
                Union12::withItem5,
                Union12::withItem6,
                Union12::withItem7,
                Union12::withItem8,
                Union12::withItem9,
                Union12::withItem10,
                Union12::withItem11);
    }
}
