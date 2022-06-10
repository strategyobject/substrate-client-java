package com.strategyobject.substrateclient.scale.readers.union;

import com.strategyobject.substrateclient.common.types.union.Union1;

public class Union1Reader extends BaseUnionReader<Union1<?>> {
    public Union1Reader() {
        super(1, Union1::withItem0);
    }
}
