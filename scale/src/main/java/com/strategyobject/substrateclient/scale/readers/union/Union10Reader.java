package com.strategyobject.substrateclient.scale.readers.union;

import com.strategyobject.substrateclient.types.union.Union10;

public class Union10Reader extends BaseUnionReader<Union10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> {
    public Union10Reader() {
        super(10,
                Union10::withItem0,
                Union10::withItem1,
                Union10::withItem2,
                Union10::withItem3,
                Union10::withItem4,
                Union10::withItem5,
                Union10::withItem6,
                Union10::withItem7,
                Union10::withItem8,
                Union10::withItem9);
    }
}
