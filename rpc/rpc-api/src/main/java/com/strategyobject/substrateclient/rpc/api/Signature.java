package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.scale.ScaleSelfWritable;

public interface Signature extends ScaleSelfWritable<Signature> {
    SignatureKind getKind();
}
