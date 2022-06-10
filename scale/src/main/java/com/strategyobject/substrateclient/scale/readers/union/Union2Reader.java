package com.strategyobject.substrateclient.scale.readers.union;

import com.strategyobject.substrateclient.common.types.union.Union2;

public class Union2Reader extends BaseUnionReader<Union2<?, ?>> {
    public Union2Reader() {
        super(2, Union2::withItem0, Union2::withItem1);
    }
}
