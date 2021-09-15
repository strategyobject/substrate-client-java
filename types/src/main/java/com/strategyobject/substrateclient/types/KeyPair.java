package com.strategyobject.substrateclient.types;

import lombok.NonNull;

import java.util.Arrays;

public class KeyPair extends FixedBytes {
    private static final int SECRET_KEY_LENGTH = 64;
    private static final int PUBLIC_KEY_LENGTH = 32;

    protected KeyPair(byte[] data) {
        super(data, SECRET_KEY_LENGTH + PUBLIC_KEY_LENGTH);
    }

    public static KeyPair fromBytes(byte @NonNull [] data) {
        return new KeyPair(data);
    }

    public SecretKey asSecretKey() {
        return SecretKey.fromBytes(Arrays.copyOfRange(getData(), 0, SECRET_KEY_LENGTH));
    }

    public PublicKey asPublicKey() {
        return PublicKey.fromBytes(Arrays.copyOfRange(getData(), SECRET_KEY_LENGTH, SECRET_KEY_LENGTH + PUBLIC_KEY_LENGTH));
    }
}
