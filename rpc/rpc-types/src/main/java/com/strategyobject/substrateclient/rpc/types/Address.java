package com.strategyobject.substrateclient.rpc.types;

import com.strategyobject.substrateclient.scale.ScaleEncoded;

public interface Address extends ScaleEncoded<Address> {
    AddressKind getKind();
}
