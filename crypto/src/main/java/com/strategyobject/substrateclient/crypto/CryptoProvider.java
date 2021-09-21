package com.strategyobject.substrateclient.crypto;

import com.strategyobject.substrateclient.types.*;
import lombok.NonNull;

public interface CryptoProvider {
    KeyPair createPairFromSeed(@NonNull Seed seed);

    SignatureData sign(@NonNull PublicKey publicKey, @NonNull SecretKey secretKey, @NonNull Signable message);

    boolean verify(@NonNull SignatureData signature, @NonNull Signable message, @NonNull PublicKey publicKey);
}
