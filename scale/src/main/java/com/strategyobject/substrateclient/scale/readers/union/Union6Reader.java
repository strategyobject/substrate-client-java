package com.strategyobject.substrateclient.scale.readers.union;

import com.strategyobject.substrateclient.types.union.Union6;

public class Union6Reader extends BaseUnionReader<Union6<?, ?, ?, ?, ?, ?>> {
    public Union6Reader() {
        super(6,
                Union6::withItem0,
                Union6::withItem1,
                Union6::withItem2,
                Union6::withItem3,
                Union6::withItem4,
                Union6::withItem5);
    }
}
