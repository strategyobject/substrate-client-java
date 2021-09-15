package com.strategyobject.substrateclient.crypto;

import com.strategyobject.substrateclient.types.*;
import lombok.NonNull;

public interface CryptoProvider {
    KeyPair createPairFromSeed(@NonNull Seed seed);

    Signature sign(@NonNull PublicKey publicKey, @NonNull SecretKey secretKey, byte @NonNull [] message);

    boolean verify(@NonNull Signature signature, byte @NonNull [] message, @NonNull PublicKey publicKey);
}
