package com.strategyobject.substrateclient.rpc.codegen.decoder;

import com.google.gson.Gson;
import com.google.testing.compile.JavaFileObjects;
import com.strategyobject.substrateclient.rpc.codegen.substitutes.TestDecodable;
import com.strategyobject.substrateclient.rpc.core.DecoderPair;
import com.strategyobject.substrateclient.rpc.core.registries.RpcDecoderRegistry;
import lombok.val;
import org.junit.jupiter.api.Test;

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

        Object source = gson.fromJson("{a: 4, b: \"123\", c: \"some\"}", Object.class);
        val expected = new TestDecodable<>(4, "123", "some");

        val actual = decoder.decode(source);

        assertEquals(gson.toJson(expected), gson.toJson(actual));
    }
}
