package com.strategyobject.substrateclient.scale;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.scale.readers.I8Reader;
import com.strategyobject.substrateclient.scale.readers.OptionReader;
import com.strategyobject.substrateclient.scale.readers.VecReader;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScaleReaderTest {

    @SneakyThrows
    @Test
    void inject() {
        // read Vec<Option<i8>>
        val reader = new VecReader().inject(new OptionReader().inject(new I8Reader()));

        val bytes = HexConverter.toBytes("0x08010100");
        val stream = new ByteArrayInputStream(bytes);
        val actual = reader.read(stream);

        assertEquals(Arrays.asList(Optional.of((byte) 1), Optional.empty()), actual);
    }
}