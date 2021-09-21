package com.strategyobject.substrateclient.rpc.types;

import com.strategyobject.substrateclient.scale.ScaleEncoded;

public interface Signature extends ScaleEncoded<Signature> {
    SignatureKind getKind();
}
