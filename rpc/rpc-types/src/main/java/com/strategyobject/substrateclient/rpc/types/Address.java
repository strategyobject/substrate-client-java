package com.strategyobject.substrateclient.rpc.types;

import com.strategyobject.substrateclient.scale.ScaleSelfWritable;

public interface Address extends ScaleSelfWritable<Address> {
    AddressKind getKind();
}
