package com.strategyobject.substrateclient.crypto.sr25519;

import com.strategyobject.substrateclient.crypto.*;
import lombok.NonNull;

public class Sr25519NativeCryptoProvider implements CryptoProvider {
    @Override
    public KeyPair createPairFromSeed(@NonNull Seed seed) {
        try {
            return KeyPair.fromBytes(Native.fromSeed(seed.getData()));
        } catch (NativeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SignatureData sign(@NonNull PublicKey publicKey, @NonNull SecretKey secretKey, @NonNull Signable message) {
        try {
            return SignatureData.fromBytes(Native.sign(publicKey.getData(), secretKey.getData(), message.getBytes()));
        } catch (NativeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verify(@NonNull SignatureData signature, @NonNull Signable message, @NonNull PublicKey publicKey) {
        try {
            return Native.verify(signature.getData(), message.getBytes(), publicKey.getData());
        } catch (NativeException e) {
            throw new RuntimeException(e);
        }
    }
}
