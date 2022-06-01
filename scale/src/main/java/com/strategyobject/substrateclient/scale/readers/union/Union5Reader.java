package com.strategyobject.substrateclient.scale.readers.union;

import com.strategyobject.substrateclient.common.types.union.Union5;

public class Union5Reader extends BaseUnionReader<Union5<?, ?, ?, ?, ?>> {
    public Union5Reader() {
        super(5,
                Union5::withItem0,
                Union5::withItem1,
                Union5::withItem2,
                Union5::withItem3,
                Union5::withItem4);
    }
}
