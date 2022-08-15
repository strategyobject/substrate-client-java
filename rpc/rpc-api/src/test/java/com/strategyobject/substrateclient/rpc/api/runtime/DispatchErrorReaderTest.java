package com.strategyobject.substrateclient.rpc.api.runtime;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import com.strategyobject.substrateclient.rpc.api.section.TestsHelper;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DispatchErrorReaderTest {

    @Test
    void readIsOther() throws IOException {
        val dispatchErrorReader = new DispatchErrorReader(TestsHelper.SCALE_READER_REGISTRY);

        val bytes = HexConverter.toBytes("0x00");
        val stream = new ByteArrayInputStream(bytes);
        val actual = dispatchErrorReader.read(stream);

        assertTrue(actual.isOther());
    }
}