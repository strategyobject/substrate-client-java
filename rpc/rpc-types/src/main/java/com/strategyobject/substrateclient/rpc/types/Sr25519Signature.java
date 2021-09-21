package com.strategyobject.substrateclient.rpc.types;

import com.strategyobject.substrateclient.types.SignatureData;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class Sr25519Signature implements Signature {
    private final SignatureKind kind = SignatureKind.SR25519;
    private final SignatureData data;

    Sr25519Signature(SignatureData data) {
        this.data = data;
    }

    public static Sr25519Signature fromBytes(byte @NonNull [] data) {
        return new Sr25519Signature(SignatureData.fromBytes(data));
    }

    public static Sr25519Signature from(@NonNull SignatureData signature) {
        return new Sr25519Signature(signature);
    }
}
