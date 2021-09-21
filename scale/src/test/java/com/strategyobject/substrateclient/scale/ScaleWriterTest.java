package com.strategyobject.substrateclient.scale;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.scale.writers.I8Writer;
import com.strategyobject.substrateclient.scale.writers.OptionWriter;
import com.strategyobject.substrateclient.scale.writers.VecWriter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScaleWriterTest {

    @SneakyThrows
    @Test
    void inject() {
        // read Vec<Option<i8>>
        val writer = new VecWriter().inject(new OptionWriter().inject(new I8Writer()));

        val stream = new ByteArrayOutputStream();
        writer.write(Arrays.asList(Optional.of((byte) 1), Optional.empty()), stream);
        val actual = HexConverter.toHex(stream.toByteArray());

        assertEquals("0x08010100", actual);
    }
}