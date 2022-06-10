package com.strategyobject.substrateclient.rpc.codegen.decoder;

import com.google.gson.Gson;
import com.google.testing.compile.JavaFileObjects;
import com.strategyobject.substrateclient.rpc.codegen.substitutes.TestDecodable;
import com.strategyobject.substrateclient.rpc.DecoderPair;
import com.strategyobject.substrateclient.rpc.registries.RpcDecoderRegistry;
import com.strategyobject.substrateclient.transport.RpcObject;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RpcDecoderProcessorTests {
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

        /*
            {
                "a": 4,
                "b": "123",
                "c": "some",
                "d": [
                    "1",
                    "2"
                ],
                "e": {
                    "a": null,
                    "b": 2
                },
                "f": "0x04000000",
                "g": "0x0c0500000002000000fdffffff",
                "h": 1.5,
            }
         */
        val source = RpcObject.of(new HashMap<String, RpcObject>() {{
            put("a", RpcObject.of(4));
            put("b", RpcObject.of("123"));
            put("c", RpcObject.of("some"));
            put("d", RpcObject.of(Arrays.asList(RpcObject.of("1"), RpcObject.of("2"))));
            put("e", RpcObject.of(new HashMap<String, RpcObject>() {{
                put("a", RpcObject.ofNull());
                put("b", RpcObject.of(2));
            }}));
            put("f", RpcObject.of("0x04000000"));
            put("g", RpcObject.of("0x0c0500000002000000fdffffff"));
            put("h", RpcObject.of(1.5));
        }});
        val actual = decoder.decode(source);

        val expected = new TestDecodable<>(4,
                "123",
                "some",
                Arrays.asList("1", "2"),
                new HashMap<String, Float>() {{
                    put("a", null);
                    put("b", 2f);
                }},
                4,
                Arrays.asList(5, 2, -3),
                1.5f);
        assertEquals(gson.toJson(expected), gson.toJson(actual));
    }
}
