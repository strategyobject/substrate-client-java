package com.strategyobject.substrateclient.scale.readers.union;

import com.strategyobject.substrateclient.types.union.Union4;

public class Union4Reader extends BaseUnionReader<Union4<?, ?, ?, ?>> {
    public Union4Reader() {
        super(4,
                Union4::withItem0,
                Union4::withItem1,
                Union4::withItem2,
                Union4::withItem3);
    }
}
