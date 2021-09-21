package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class I128ReaderTest {
    private final I128Reader i128Reader = new I128Reader();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({
            "0x00000000000000000000000000000000,0",
            "0x45000000000000000000000000000000,69",
            "0x00000000000000000000000000000080,-170141183460469231731687303715884105728",
            "0xffffffffffffffffffffffffffffff7f,170141183460469231731687303715884105727"
    })
    void read(String input, String expected) {
        val bytes = HexConverter.toBytes(input);
        val stream = new ByteArrayInputStream(bytes);
        val actual = i128Reader.read(stream);
        assertEquals(new BigInteger(expected), actual);
    }
}
