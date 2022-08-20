package com.strategyobject.substrateclient.scale;

import com.strategyobject.substrateclient.common.inject.Injection;
import com.strategyobject.substrateclient.common.types.Result;
import com.strategyobject.substrateclient.common.types.tuple.Pair;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ScaleUtilsTest {

    @Test
    void toBytes() {
        val value = Result.ok(Pair.of(Arrays.asList(true, false), 10));

        val bytes = ScaleUtils.toBytes(
                value,
                new ScaleWriterRegistry(),
                Injection.of(Result.class).inject(
                        Injection.of(Pair.class).inject(
                                Injection.of(List.class).inject(Boolean.class),
                                ScaleDispatch.class),
                        Void.class));

        assertArrayEquals(new byte[]{0, 8, 1, 0, 10, 0, 0, 0}, bytes);
    }
}