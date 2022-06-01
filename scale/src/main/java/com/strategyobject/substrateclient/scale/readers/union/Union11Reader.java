package com.strategyobject.substrateclient.scale.readers.union;

import com.strategyobject.substrateclient.common.types.union.Union11;

public class Union11Reader extends BaseUnionReader<Union11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> {
    public Union11Reader() {
        super(11,
                Union11::withItem0,
                Union11::withItem1,
                Union11::withItem2,
                Union11::withItem3,
                Union11::withItem4,
                Union11::withItem5,
                Union11::withItem6,
                Union11::withItem7,
                Union11::withItem8,
                Union11::withItem9,
                Union11::withItem10);
    }
}
