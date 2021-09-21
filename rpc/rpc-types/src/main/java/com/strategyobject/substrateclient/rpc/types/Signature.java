package com.strategyobject.substrateclient.rpc.types;

import com.strategyobject.substrateclient.scale.ScaleSelfWritable;

public interface Signature extends ScaleSelfWritable<Signature> {
    SignatureKind getKind();
}
