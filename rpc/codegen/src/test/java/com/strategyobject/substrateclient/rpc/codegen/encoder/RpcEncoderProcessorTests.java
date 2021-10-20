package com.strategyobject.substrateclient.rpc.codegen.encoder;

import com.google.gson.Gson;
import com.google.testing.compile.JavaFileObjects;
import com.strategyobject.substrateclient.rpc.codegen.substitutes.TestEncodable;
import com.strategyobject.substrateclient.rpc.core.EncoderPair;
import com.strategyobject.substrateclient.rpc.core.RpcEncoder;
import com.strategyobject.substrateclient.rpc.core.registries.RpcEncoderRegistry;
import com.strategyobject.substrateclient.scale.ScaleUtils;
import com.strategyobject.substrateclient.scale.writers.I32Writer;
import lombok.val;
import org.junit.jupiter.api.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RpcEncoderProcessorTests {
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
        val registry = RpcEncoderRegistry.getInstance();
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

        val source = new TestEncodable<>(4, "some", new TestEncodable.Subclass<>(123), true);
        val expected = gson.fromJson(
                "{\"a\":\"" + ScaleUtils.toHexString(4, new I32Writer()) + "\", \"b\": \"some\", \"c\": {\"a\": 123}, \"d\": true}",
                Object.class);

        val actual = gson.fromJson(
                gson.toJson(encoder.encode(source)),
                Object.class);

        assertEquals(gson.toJson(expected), gson.toJson(actual));
    }
}
