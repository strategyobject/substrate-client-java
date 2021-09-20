package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.common.utils.Convert;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class I128WriterTest {
    private final I128Writer i128Writer = new I128Writer();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({
            "0,0x00000000000000000000000000000000",
            "69,0x45000000000000000000000000000000",
            "-170141183460469231731687303715884105728,0x00000000000000000000000000000080",
            "170141183460469231731687303715884105727,0xffffffffffffffffffffffffffffff7f"
    })
    void write(String input, String expected) {
        val stream = new ByteArrayOutputStream();
        i128Writer.write(new BigInteger(input), stream);
        val actual = Convert.toHex(stream.toByteArray());
        assertEquals(expected, actual);
    }
}