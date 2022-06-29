package com.strategyobject.substrateclient.rpc.codegen.encoder;

import com.google.gson.Gson;
import com.google.testing.compile.JavaFileObjects;
import com.strategyobject.substrateclient.rpc.EncoderPair;
import com.strategyobject.substrateclient.rpc.RpcEncoder;
import com.strategyobject.substrateclient.rpc.codegen.substitutes.TestEncodable;
import com.strategyobject.substrateclient.rpc.registries.RpcEncoderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
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

class RpcEncoderProcessorTests {
    private final Gson gson = new Gson();

    @Test
    void failsWhenAnnotationIsAppliedToClass() {
        val clazz = JavaFileObjects.forResource("RpcEncodableInterface.java");

        val compilation = javac()
                .withProcessors(new RpcEncoderProcessor())
                .compile(clazz);

        assertThat(compilation).failed();
        assertThat(compilation)
                .hadErrorContaining("Only classes");
    }

    @Test
    void failsWhenDoesNotHaveGetter() {
        val clazz = JavaFileObjects.forResource("RpcEncodableWithoutGetter.java");

        val compilation = javac()
                .withProcessors(new RpcEncoderProcessor())
                .compile(clazz);

        assertThat(compilation).failed();
    }

    @Test
    void compiles() {
        val clazz = JavaFileObjects.forResource("RpcEncodable.java");

        val compilation = javac()
                .withProcessors(new RpcEncoderProcessor())
                .compile(clazz);

        assertThat(compilation).succeeded();
    }

    @Test
    @SuppressWarnings("unchecked")
    void compilesAndDecodes() {  // TODO move this test out of the project
        val registry = new RpcEncoderRegistry(new ScaleWriterRegistry());
        registry.registerAnnotatedFrom("com.strategyobject.substrateclient.rpc.codegen.substitutes");

        val encoder = (RpcEncoder<TestEncodable<?>>) registry.resolve(TestEncodable.class)
                .inject(
                        EncoderPair.of(
                                registry
                                        .resolve(TestEncodable.Subclass.class)
                                        .inject(EncoderPair.of(
                                                registry.resolve(int.class),
                                                null
                                        )),
                                null));

        val source = new TestEncodable<>(4,
                "some",
                new TestEncodable.Subclass<>(123),
                true,
                Arrays.asList("a", "b"),
                Stream.of(
                                new AbstractMap.SimpleEntry<>("a", 1),
                                new AbstractMap.SimpleEntry<>("b", 2))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
                Arrays.asList(2, 3));
        val expected = gson.fromJson(
                "{\"a\":\"0x04000000\",\"b\":\"some\",\"c\":{\"a\":123},\"d\":true," +
                        "\"e\":[\"a\",\"b\"],\"f\":{\"a\":1,\"b\":2},\"h\":\"0x080200000003000000\"}",
                Object.class);

        val actual = gson.fromJson(
                gson.toJson(encoder.encode(source)),
                Object.class);

        assertEquals(gson.toJson(expected), gson.toJson(actual));
    }
}
