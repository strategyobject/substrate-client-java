package com.strategyobject.substrateclient.scale.codegen.writer;

import com.google.testing.compile.JavaFileObjects;
import lombok.val;
import org.junit.jupiter.api.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

class ScaleWriterProcessorTest {
    @Test
    void failsWhenWrongTemplate() {
        val nameless = JavaFileObjects.forResource("WrongTemplate.java");

        val compilation = javac()
                .withProcessors(new ScaleWriterProcessor())
                .compile(nameless);

        assertThat(compilation).failed();
        assertThat(compilation)
                .hadErrorContaining("Wrong template");
    }

    @Test
    void failsWhenMissesScaleSelfWritable() {
        val nameless = JavaFileObjects.forResource("MissesScaleSelfWritable.java");

        val compilation = javac()
                .withProcessors(new ScaleWriterProcessor())
                .compile(nameless);

        assertThat(compilation).failed();
        assertThat(compilation)
                .hadErrorContaining("ScaleSelfWritable");
    }

    @Test
    void compilesNonGenericScaleSelfWritable() {
        val clazz = JavaFileObjects.forResource("NonGenericScaleSelfWritable.java");

        val compilation = javac()
                .withProcessors(new ScaleWriterProcessor())
                .compile(clazz);

        assertThat(compilation).succeeded();
    }

    @Test
    void compilesGenericScaleSelfWritable() {
        val clazz = JavaFileObjects.forResource("GenericScaleSelfWritable.java");

        val compilation = javac()
                .withProcessors(new ScaleWriterProcessor())
                .compile(clazz);

        assertThat(compilation).succeeded();
    }

    @Test
    void compilesAnnotated() {
        val clazz = JavaFileObjects.forResource("Annotated.java");

        val compilation = javac()
                .withProcessors(new ScaleWriterProcessor())
                .compile(clazz);

        assertThat(compilation).succeeded();
    }

    @Test
    void compilesNonAnnotated() {
        val clazz = JavaFileObjects.forResource("NonAnnotated.java");

        val compilation = javac()
                .withProcessors(new ScaleWriterProcessor())
                .compile(clazz);

        assertThat(compilation).succeeded();
    }

    @Test
    void compilesComplexGeneric() {
        val clazz = JavaFileObjects.forResource("ComplexGeneric.java");

        val compilation = javac()
                .withProcessors(new ScaleWriterProcessor())
                .compile(clazz);

        assertThat(compilation).succeeded();
    }

    @Test
    void compilesArrays() {
        val clazz = JavaFileObjects.forResource("Arrays.java");

        val compilation = javac()
                .withProcessors(new ScaleWriterProcessor())
                .compile(clazz);

        assertThat(compilation).succeeded();
    }
}