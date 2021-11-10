package com.strategyobject.substrateclient.scale.readers.union;

import com.strategyobject.substrateclient.types.union.Union9;

public class Union9Reader extends BaseUnionReader<Union9<?, ?, ?, ?, ?, ?, ?, ?, ?>> {
    public Union9Reader() {
        super(9,
                Union9::withItem0,
                Union9::withItem1,
                Union9::withItem2,
                Union9::withItem3,
                Union9::withItem4,
                Union9::withItem5,
                Union9::withItem6,
                Union9::withItem7,
                Union9::withItem8);
    }
}
