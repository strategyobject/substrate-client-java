package com.strategyobject.substrateclient.scale.readers.union;

import com.strategyobject.substrateclient.types.union.Union7;

public class Union7Reader extends BaseUnionReader<Union7<?, ?, ?, ?, ?, ?, ?>> {
    public Union7Reader() {
        super(7,
                Union7::withItem0,
                Union7::withItem1,
                Union7::withItem2,
                Union7::withItem3,
                Union7::withItem4,
                Union7::withItem5,
                Union7::withItem6);
    }
}
