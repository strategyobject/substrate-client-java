package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.scale.ScaleSelfWritable;

public interface Address extends ScaleSelfWritable<Address> {
    AddressKind getKind();
}
