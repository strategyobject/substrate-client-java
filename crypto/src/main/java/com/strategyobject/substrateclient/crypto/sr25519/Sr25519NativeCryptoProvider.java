package com.strategyobject.substrateclient.crypto.sr25519;

import com.strategyobject.substrateclient.common.types.Bytes;
import com.strategyobject.substrateclient.crypto.*;
import lombok.NonNull;

public class Sr25519NativeCryptoProvider implements CryptoProvider {
    @Override
    public KeyPair createPairFromSeed(@NonNull Seed seed) {
        try {
            return KeyPair.fromBytes(Native.fromSeed(seed.getBytes()));
        } catch (NativeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SignatureData sign(@NonNull PublicKey publicKey, @NonNull SecretKey secretKey, @NonNull Bytes message) {
        try {
            return SignatureData.fromBytes(Native.sign(publicKey.getBytes(), secretKey.getBytes(), message.getBytes()));
        } catch (NativeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verify(@NonNull SignatureData signature, @NonNull Bytes message, @NonNull PublicKey publicKey) {
        try {
            return Native.verify(signature.getBytes(), message.getBytes(), publicKey.getBytes());
        } catch (NativeException e) {
            throw new RuntimeException(e);
        }
    }
}
