package com.strategyobject.substrateclient.pallet.codegen;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import com.strategyobject.substrateclient.pallet.codegen.PalletInterfaceProcessor;
import lombok.val;
import org.junit.jupiter.api.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

class PalletInterfaceProcessorTests {
    @Test
    void failsWhenAnnotationIsAppliedToClass() {
        val clazz = JavaFileObjects.forResource("ClassPallet.java");

        val compilation = javac()
                .withProcessors(new PalletInterfaceProcessor())
                .compile(clazz);

        assertThat(compilation).failed();
        assertThat(compilation)
                .hadErrorContaining("Only interfaces");
    }

    @Test
    void failsWhenPalletHasEmptyName() {
        val clazz = JavaFileObjects.forResource("UnnamedPallet.java");

        val compilation = javac()
                .withProcessors(new PalletInterfaceProcessor())
                .compile(clazz);

        assertThat(compilation).failed();
        assertThat(compilation)
                .hadErrorContaining("contains null or empty `value`");
    }

    @Test
    void failsWhenStorageHasEmptyName() {
        val clazz = JavaFileObjects.forResource("UnnamedStorage.java");

        val compilation = javac()
                .withProcessors(new PalletInterfaceProcessor())
                .compile(clazz);

        assertThat(compilation).failed();
        assertThat(compilation)
                .hadErrorContaining("doesn't have `value`");
    }

    @Test
    void failsWhenScaleIsNotSet() {
        val clazz = JavaFileObjects.forResource("WithoutScale.java");

        val compilation = javac()
                .withProcessors(new PalletInterfaceProcessor())
                .compile(clazz);

        assertThat(compilation).failed();
        assertThat(compilation)
                .hadErrorContaining("Please set either `type` or `generic` parameter of @StorageKey");
    }

    @Test
    void failsWhenScaleTypeOfKeyIsAmbiguous() {
        val clazz = JavaFileObjects.forResource("AmbiguousScale.java");

        val compilation = javac()
                .withProcessors(new PalletInterfaceProcessor())
                .compile(clazz);

        assertThat(compilation).failed();
        assertThat(compilation)
                .hadErrorContaining("Please set only one parameter of @StorageKey: `type` or `generic`.");
    }

    @Test
    void failsWhenStorageMethodReturnsIncorrectType() {
        val clazz = JavaFileObjects.forResource("NotAStorage.java");

        val compilation = javac()
                .withProcessors(new PalletInterfaceProcessor())
                .compile(clazz);

        assertThat(compilation).failed();
        assertThat(compilation)
                .hadErrorContaining("has incorrect return type");
    }

    @Test
    void compiles() {
        val generatedName = "TestPalletImpl";

        val clazz = JavaFileObjects.forResource("TestPallet.java");
        val compilation = javac()
                .withProcessors(new PalletInterfaceProcessor())
                .compile(clazz);

        assertThat(compilation).succeeded();

        assertContains(generatedName, compilation, "private final State state;");
        assertContains(generatedName, compilation, "private final StorageNMap<Integer> value;");
        assertContains(generatedName, compilation, "private final StorageNMap<Integer> map;");
        assertContains(generatedName, compilation, "private final StorageNMap<AccountId> doubleMap;");
        assertContains(generatedName, compilation, "private final StorageNMap<Integer> tripleMap;");
        assertContains(generatedName, compilation, "public TestPalletImpl(State state)");
        assertContains(generatedName, compilation, "public StorageNMap<Integer> value()");
        assertContains(generatedName, compilation, "public StorageNMap<Integer> map()");
        assertContains(generatedName, compilation, "public StorageNMap<AccountId> doubleMap()");
        assertContains(generatedName, compilation, "public StorageNMap<Integer> tripleMap()");
    }

    private void assertContains(String className, Compilation compilation, String target) {
        assertThat(compilation)
                .generatedSourceFile(className)
                .contentsAsUtf8String()
                .contains(target);
    }
}
