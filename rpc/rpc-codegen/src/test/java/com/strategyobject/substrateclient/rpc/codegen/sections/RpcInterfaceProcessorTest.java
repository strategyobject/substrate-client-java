package com.strategyobject.substrateclient.rpc.codegen.sections;

import com.google.testing.compile.JavaFileObjects;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcCall;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcSubscription;
import lombok.val;
import org.junit.jupiter.api.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

class RpcInterfaceProcessorTest {
    @Test
    void failsWhenAnnotationHasEmptySection() {
        val nameless = JavaFileObjects.forResource("NamelessSection.java");

        val compilation = javac()
                .withProcessors(new RpcInterfaceProcessor())
                .compile(nameless);

        assertThat(compilation).failed();
        assertThat(compilation)
                .hadErrorContaining("null or empty `value`");
    }

    @Test
    void failsWhenAnnotationIsAppliedToClass() {
        val clazz = JavaFileObjects.forResource("ClassSection.java");

        val compilation = javac()
                .withProcessors(new RpcInterfaceProcessor())
                .compile(clazz);

        assertThat(compilation).failed();
        assertThat(compilation)
                .hadErrorContaining("Only interfaces");
    }

    @Test
    void failsWhenMethodIsNotAnnotated() {
        val clazz = JavaFileObjects.forResource("SectionWithoutAnnotatedMethod.java");

        val compilation = javac()
                .withProcessors(new RpcInterfaceProcessor())
                .compile(clazz);

        assertThat(compilation).failed();
        assertThat(compilation)
                .hadErrorContaining(
                        String.format(
                                "doesn't have `@%s` or `@%s` annotation",
                                RpcCall.class.getSimpleName(),
                                RpcSubscription.class.getSimpleName()));
    }

    @Test
    void failsWhenMethodHasAmbiguousAnnotations() {
        val clazz = JavaFileObjects.forResource("SectionWithAmbiguousAnnotatedMethod.java");

        val compilation = javac()
                .withProcessors(new RpcInterfaceProcessor())
                .compile(clazz);

        assertThat(compilation).failed();
        assertThat(compilation)
                .hadErrorContaining("it has ambiguous annotations");
    }

    @Test
    void failsWhenReturnTypeOfMethodIsIncorrect() {
        val clazz = JavaFileObjects.forResource("SectionWithIncorrectReturnOfMethod.java");

        val compilation = javac()
                .withProcessors(new RpcInterfaceProcessor())
                .compile(clazz);

        assertThat(compilation).failed();
        assertThat(compilation)
                .hadErrorContaining("has unexpected return type");
    }

    @Test
    void failsWhenReturnTypeOfSubscriptionIsIncorrect() {
        val clazz = JavaFileObjects.forResource("SectionWithIncorrectReturnOfSubscription.java");

        val compilation = javac()
                .withProcessors(new RpcInterfaceProcessor())
                .compile(clazz);

        assertThat(compilation).failed();
        assertThat(compilation)
                .hadErrorContaining("has unexpected return type");
    }

    @Test
    void failsWhenCallbackDoesNotExist() {
        val clazz = JavaFileObjects.forResource("SectionWithoutCallback.java");

        val compilation = javac()
                .withProcessors(new RpcInterfaceProcessor())
                .compile(clazz);

        assertThat(compilation).failed();
        assertThat(compilation)
                .hadErrorContaining("doesn't contain a callback");
    }

    @Test
    void failsWhenMethodHasManyCallbacks() {
        val clazz = JavaFileObjects.forResource("SectionWithManyCallbacks.java");

        val compilation = javac()
                .withProcessors(new RpcInterfaceProcessor())
                .compile(clazz);

        assertThat(compilation).failed();
        assertThat(compilation)
                .hadErrorContaining("contains more than one callback");
    }

    @Test
    void compilesTestSection() {
        val clazz = JavaFileObjects.forResource("TestSection.java");

        val compilation = javac()
                .withProcessors(new RpcInterfaceProcessor())
                .compile(clazz);

        assertThat(compilation).succeeded();
    }
}
