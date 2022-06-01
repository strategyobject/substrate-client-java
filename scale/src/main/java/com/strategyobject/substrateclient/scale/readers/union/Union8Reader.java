package com.strategyobject.substrateclient.scale.readers.union;

import com.strategyobject.substrateclient.common.types.union.Union8;

public class Union8Reader extends BaseUnionReader<Union8<?, ?, ?, ?, ?, ?, ?, ?>> {
    public Union8Reader() {
        super(8,
                Union8::withItem0,
                Union8::withItem1,
                Union8::withItem2,
                Union8::withItem3,
                Union8::withItem4,
                Union8::withItem5,
                Union8::withItem6,
                Union8::withItem7);
    }
}
