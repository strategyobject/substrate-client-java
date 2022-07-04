package com.strategyobject.substrateclient.crypto;

import com.strategyobject.substrateclient.common.types.Bytes;
import lombok.NonNull;

public interface CryptoProvider {
    KeyPair createPairFromSeed(@NonNull Seed seed);

    SignatureData sign(@NonNull PublicKey publicKey, @NonNull SecretKey secretKey, @NonNull Bytes message);

    boolean verify(@NonNull SignatureData signature, @NonNull Bytes message, @NonNull PublicKey publicKey);
}
