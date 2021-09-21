package com.strategyobject.substrateclient.crypto;

import com.strategyobject.substrateclient.crypto.sr25519.Sr25519NativeCryptoProvider;
import com.strategyobject.substrateclient.types.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;

public class KeyRing {
    @Getter
    private final SecretKey secretKey;
    @Getter
    private final PublicKey publicKey;

    private final CryptoProvider cryptoProvider = new Sr25519NativeCryptoProvider();

    protected KeyRing(SecretKey secretKey, PublicKey publicKey) {
        this.secretKey = secretKey;
        this.publicKey = publicKey;
    }

    protected KeyRing(Seed seed) {
        val keyPair = cryptoProvider.createPairFromSeed(seed);
        this.secretKey = keyPair.asSecretKey();
        this.publicKey = keyPair.asPublicKey();
    }

    public static KeyRing fromKeyPair(@NonNull KeyPair keyPair) {
        return new KeyRing(keyPair.asSecretKey(), keyPair.asPublicKey());
    }

    public static KeyRing fromSeed(@NonNull Seed seed) {
        return new KeyRing(seed);
    }

    public SignatureData sign(@NonNull Signable message) {
        return cryptoProvider.sign(publicKey, secretKey, message);
    }

    public boolean verifyOwn(@NonNull SignatureData signature, @NonNull Signable message) {
        return cryptoProvider.verify(signature, message, publicKey);
    }

    public boolean verify(@NonNull SignatureData signature, @NonNull Signable message, @NonNull PublicKey publicKey) {
        return cryptoProvider.verify(signature, message, publicKey);
    }
}
