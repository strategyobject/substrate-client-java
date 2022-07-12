package com.strategyobject.substrateclient.crypto.ss58;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SS58CodecTests {
    @ParameterizedTest
    @CsvSource(value = {
            "5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY:0xd43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d:42",
            "cEaNSpz4PxFcZ7nT1VEKrKewH67rfx6MfcM6yKojyyPz7qaqp:0xd43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d:64",
            "yGHXkYLYqxijLKKfd9Q2CB9shRVu8rPNBS53wvwGTutYg4zTg:0xd43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d:255",
            "VByeGLMtP8r8BYQpNX1sb2VtAW8GYCbtFAeXJwsA2ur3MNRdq:0xd43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d:256",
            "yNa8JpqfFB3q8A29rCwSgxvdU94ufJw2yKKxDgznS5m1PoFvn:0xd43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d:16383",
            "5FHneW46xGXgs5mUiveU4sbTyGBzmstUspZC92UhjJM694ty:0x8eaf04151687736326c9fea17e25fc5287613693c912909cb226aa4794f26a48:42"
    },
            delimiterString = ":")
    void encode(String expected, String hex, short prefix) {
        val actual = SS58Codec.encode(HexConverter.toBytes(hex), SS58AddressFormat.of(prefix));

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0xd4:42",
            "0x:42",
            "0xd43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d:16384"
    },
            delimiterString = ":")
    void encodeThrows(String hex, short prefix) {
        val format = SS58AddressFormat.of(prefix);
        val address = HexConverter.toBytes(hex);
        assertThrows(IllegalArgumentException.class, () -> SS58Codec.encode(address, format));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY:0xd43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d:42",
            "cEaNSpz4PxFcZ7nT1VEKrKewH67rfx6MfcM6yKojyyPz7qaqp:0xd43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d:64",
            "yGHXkYLYqxijLKKfd9Q2CB9shRVu8rPNBS53wvwGTutYg4zTg:0xd43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d:255",
            "VByeGLMtP8r8BYQpNX1sb2VtAW8GYCbtFAeXJwsA2ur3MNRdq:0xd43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d:256",
            "yNa8JpqfFB3q8A29rCwSgxvdU94ufJw2yKKxDgznS5m1PoFvn:0xd43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d:16383",
            "5FHneW46xGXgs5mUiveU4sbTyGBzmstUspZC92UhjJM694ty:0x8eaf04151687736326c9fea17e25fc5287613693c912909cb226aa4794f26a48:42"
    },
            delimiterString = ":")
    void decode(String source, String hex, short prefix) {
        val actual = SS58Codec.decode(source);

        val expected = AddressWithPrefix.from(HexConverter.toBytes(hex), SS58AddressFormat.of(prefix));
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "x", // incorrect length (too short)
            "yA3vprfzKUKan9P1eXE6iMGCMSMDZEnAtb6wEjTEf86eMi", // incorrect length (last byte is lost)
            "SXYSytZ7wxpQHbRo5FzUFA9wjTfWvTQgYzhVEybWRQvBrhwW", // unknown version (first byte is out of range)
            "5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutL8" // incorrect checksum (second to last byte is different)
    })
    void decodeThrows(String encoded) {
        assertThrows(IllegalArgumentException.class, () -> SS58Codec.decode(encoded));
    }
}
