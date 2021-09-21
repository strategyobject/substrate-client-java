package com.strategyobject.substrateclient.crypto.sr25519;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.crypto.NativeException;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

import static com.strategyobject.substrateclient.crypto.sr25519.Native.*;
import static org.junit.jupiter.api.Assertions.*;

public class NativeTests {
    @Test
    void deriveKeyPairHard() throws NativeException {
        val aliceChainCode = HexConverter.toBytes("14416c6963650000000000000000000000000000000000000000000000000000");
        val seed = HexConverter.toBytes("fac7959dbfe72f052e5a0c3c8d6530f202b02fd8f9f5ca3580ec8deb7797479e");
        val expected = HexConverter.toBytes("d43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d");

        val keyPair = Native.fromSeed(seed);
        val derived = Native.deriveKeyPairHard(keyPair, aliceChainCode);
        val publicKey = Arrays.copyOfRange(derived, SECRET_KEY_LENGTH, KEYPAIR_LENGTH);

        assertArrayEquals(expected, publicKey);
    }

    @Test
    void deriveKeyPairSoft() throws NativeException {
        val fooChainCode = HexConverter.toBytes("0c666f6f00000000000000000000000000000000000000000000000000000000");
        val seed = HexConverter.toBytes("fac7959dbfe72f052e5a0c3c8d6530f202b02fd8f9f5ca3580ec8deb7797479e");
        val expected = HexConverter.toBytes("40b9675df90efa6069ff623b0fdfcf706cd47ca7452a5056c7ad58194d23440a");

        val keyPair = Native.fromSeed(seed);
        val derived = Native.deriveKeyPairSoft(keyPair, fooChainCode);
        val publicKey = Arrays.copyOfRange(derived, SECRET_KEY_LENGTH, KEYPAIR_LENGTH);

        assertArrayEquals(expected, publicKey);
    }

    @Test
    void derivePublicSoft() throws NativeException {
        val fooChainCode = HexConverter.toBytes("0c666f6f00000000000000000000000000000000000000000000000000000000");
        val publicKey = HexConverter.toBytes("46ebddef8cd9bb167dc30878d7113b7e168e6f0646beffd77d69d39bad76b47a");
        val expected = HexConverter.toBytes("40b9675df90efa6069ff623b0fdfcf706cd47ca7452a5056c7ad58194d23440a");

        val derived = Native.derivePublicSoft(publicKey, fooChainCode);

        assertArrayEquals(expected, derived);
    }

    @Test
    void fromSeed() throws NativeException {
        val seed = HexConverter.toBytes("fac7959dbfe72f052e5a0c3c8d6530f202b02fd8f9f5ca3580ec8deb7797479e");
        val expected = HexConverter.toBytes("46ebddef8cd9bb167dc30878d7113b7e168e6f0646beffd77d69d39bad76b47a");

        val keyPair = Native.fromSeed(seed);
        val publicKey = Arrays.copyOfRange(keyPair, SECRET_KEY_LENGTH, KEYPAIR_LENGTH);

        assertArrayEquals(expected, publicKey);
    }

    @Test
    void fromPair() throws NativeException {
        val sourcePair = HexConverter.toBytes("28b0ae221c6bb06856b287f60d7ea0d98552ea5a16db16956849aa371db3eb51fd190" +
                "cce74df356432b410bd64682309d6dedb27c76845daf388557cbac3ca3446ebddef8cd9bb167dc30878d7113b7e168e6f0646be" +
                "ffd77d69d39bad76b47a");
        val expected = HexConverter.toBytes("46ebddef8cd9bb167dc30878d7113b7e168e6f0646beffd77d69d39bad76b47a");

        val keyPair = Native.fromPair(sourcePair);
        val publicKey = Arrays.copyOfRange(keyPair, SECRET_KEY_LENGTH, KEYPAIR_LENGTH);

        assertArrayEquals(expected, publicKey);
    }

    @Test
    void sign() throws NativeException {
        val seed = generateRandomSeed();
        val keyPair = Native.fromSeed(seed);
        val secretKey = Arrays.copyOfRange(keyPair, 0, SECRET_KEY_LENGTH);
        val publicKey = Arrays.copyOfRange(keyPair, SECRET_KEY_LENGTH, KEYPAIR_LENGTH);
        val message = "This is a message".getBytes(StandardCharsets.UTF_8);
        val signature = Native.sign(publicKey, secretKey, message);

        assertEquals(SIGNATURE_LENGTH, signature.length);
    }

    @Test
    void verify() throws NativeException {
        val seed = generateRandomSeed();
        val keyPair = Native.fromSeed(seed);
        val secretKey = Arrays.copyOfRange(keyPair, 0, SECRET_KEY_LENGTH);
        val publicKey = Arrays.copyOfRange(keyPair, SECRET_KEY_LENGTH, KEYPAIR_LENGTH);
        val message = "This is a message".getBytes(StandardCharsets.UTF_8);
        val signature = Native.sign(publicKey, secretKey, message);

        val isValid = Native.verify(signature, message, publicKey);

        assertTrue(isValid);
    }

    @Test
    void agree() throws NativeException {
        val seed = HexConverter.toBytes("98b3d305d5a5eace562387e47e59badd4d77e3f72cabfb10a60f8a197059f0a8");
        val otherSeed = HexConverter.toBytes("9732eea001851ff862d949a1699c9971f3a26edbede2ad7922cbbe9a0701f366");
        val expected = HexConverter.toBytes("b03a0b198c34c16f35cae933d88b16341b4cef3e84e851f20e664c6a30527f4e");

        val keyPair = Native.fromSeed(seed);
        val secretKey = Arrays.copyOfRange(keyPair, 0, SECRET_KEY_LENGTH);
        val publicKey = Arrays.copyOfRange(keyPair, SECRET_KEY_LENGTH, KEYPAIR_LENGTH);

        val otherKeyPair = Native.fromSeed(otherSeed);
        val otherSecretKey = Arrays.copyOfRange(otherKeyPair, 0, SECRET_KEY_LENGTH);
        val otherPublicKey = Arrays.copyOfRange(otherKeyPair, SECRET_KEY_LENGTH, KEYPAIR_LENGTH);

        val publicAndOtherSecret = Native.agree(publicKey, otherSecretKey);
        val otherPublicAndSecret = Native.agree(otherPublicKey, secretKey);

        assertArrayEquals(publicAndOtherSecret, otherPublicAndSecret);
        assertArrayEquals(expected, publicAndOtherSecret);
    }

    private byte[] generateRandomSeed() {
        val seed = new byte[32];

        val rnd = new Random();
        rnd.nextBytes(seed);

        return seed;
    }
}
