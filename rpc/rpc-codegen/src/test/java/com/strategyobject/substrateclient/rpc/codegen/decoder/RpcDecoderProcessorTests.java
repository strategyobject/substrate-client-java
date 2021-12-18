package com.strategyobject.substrateclient.rpc.codegen.decoder;

import com.google.gson.Gson;
import com.google.testing.compile.JavaFileObjects;
import com.strategyobject.substrateclient.rpc.codegen.substitutes.TestDecodable;
import com.strategyobject.substrateclient.rpc.core.DecoderPair;
import com.strategyobject.substrateclient.rpc.core.registries.RpcDecoderRegistry;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RpcDecoderProcessorTests {
    private final Gson gson = new Gson();

    @Test
    void failsWhenAnnotationIsAppliedToClass() {
        val clazz = JavaFileObjects.forResource("RpcDecodableInterface.java");

        val compilation = javac()
                .withProcessors(new RpcDecoderProcessor())
                .compile(clazz);

        assertThat(compilation).failed();
        assertThat(compilation)
                .hadErrorContaining("Only classes");
    }

    @Test
    void failsWhenDoesNotHaveSetter() {
        val clazz = JavaFileObjects.forResource("RpcDecodableWithoutSetter.java");

        val compilation = javac()
                .withProcessors(new RpcDecoderProcessor())
                .compile(clazz);

        assertThat(compilation).failed();
    }

    @Test
    void compiles() {
        val clazz = JavaFileObjects.forResource("RpcDecodable.java");

        val compilation = javac()
                .withProcessors(new RpcDecoderProcessor())
                .compile(clazz);

        assertThat(compilation).succeeded();
    }

    @Test
    void compilesAndDecodes() { // TODO move this test out of the project
        val registry = RpcDecoderRegistry.getInstance();
        val decoder = registry.resolve(TestDecodable.class)
                .inject(DecoderPair.of(registry.resolve(String.class), null));

        Object source = gson.fromJson("{\"a\":4,\"b\":\"123\",\"c\":\"some\"," +
                        "\"d\":[\"1\",\"2\"],\"e\":{\"a\":1,\"b\":2},\"f\":\"0x04000000\"," +
                        "\"g\":\"0x0c0500000002000000fdffffff\"}",
                Object.class);
        val expected = new TestDecodable<>(4,
                "123",
                "some",
                Arrays.asList("1", "2"),
                Stream.of(
                                new AbstractMap.SimpleEntry<>("a", 1),
                                new AbstractMap.SimpleEntry<>("b", 2))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
                4,
                Arrays.asList(5, 2, -3));

        val actual = decoder.decode(source);

        assertEquals(gson.toJson(expected), gson.toJson(actual));
    }
}
