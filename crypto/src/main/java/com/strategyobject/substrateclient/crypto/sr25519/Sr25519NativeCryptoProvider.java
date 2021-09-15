package com.strategyobject.substrateclient.crypto.sr25519;

import com.strategyobject.substrateclient.crypto.CryptoProvider;
import com.strategyobject.substrateclient.crypto.NativeException;
import com.strategyobject.substrateclient.types.*;
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
    public Signature sign(@NonNull PublicKey publicKey, @NonNull SecretKey secretKey, byte @NonNull [] message) {
        try {
            return Signature.fromBytes(Native.sign(publicKey.getData(), secretKey.getData(), message));
        } catch (NativeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verify(@NonNull Signature signature, byte @NonNull [] message, @NonNull PublicKey publicKey) {
        try {
            return Native.verify(signature.getData(), message, publicKey.getData());
        } catch (NativeException e) {
            throw new RuntimeException(e);
        }
    }
}
