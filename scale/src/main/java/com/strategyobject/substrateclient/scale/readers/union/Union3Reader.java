package com.strategyobject.substrateclient.scale.readers.union;

import com.strategyobject.substrateclient.common.types.union.Union3;

public class Union3Reader extends BaseUnionReader<Union3<?, ?, ?>> {
    public Union3Reader() {
        super(3,
                Union3::withItem0,
                Union3::withItem1,
                Union3::withItem2);
    }
}
