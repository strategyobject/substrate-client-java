package com.strategyobject.substrateclient.pallet.codegen;

import com.google.common.base.Strings;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.strategyobject.substrateclient.common.codegen.ProcessingException;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.rpc.api.section.State;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.val;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.stream.Stream;

import static com.strategyobject.substrateclient.pallet.codegen.Constants.*;

public class PalletAnnotatedInterface {
    private final TypeElement interfaceElement;
    private final String name;
    private final PalletMethodProcessor methodProcessor;

    public PalletAnnotatedInterface(TypeElement interfaceElement, PalletMethodProcessor methodProcessor) throws ProcessingException {
        this.interfaceElement = interfaceElement;
        val annotation = interfaceElement.getAnnotation(Pallet.class);

        if (!interfaceElement.getModifiers().contains(Modifier.PUBLIC)) {
            throw new ProcessingException(
                    interfaceElement,
                    "`%s` is not public. That is not allowed.",
                    interfaceElement.getQualifiedName().toString());
        }

        if (Strings.isNullOrEmpty(name = annotation.value())) {
            throw new ProcessingException(
                    interfaceElement,
                    "`@%s` of `%s` contains null or empty `value`.",
                    annotation.getClass().getSimpleName(),
                    interfaceElement.getQualifiedName().toString());
        }

        this.methodProcessor = methodProcessor;
    }

    public void generateClass(ProcessorContext context) throws ProcessingException, IOException {
        val interfaceName = interfaceElement.getSimpleName().toString();
        val className = String.format(CLASS_NAME_TEMPLATE, interfaceName);
        val packageName = context.getPackageName(interfaceElement);

        val typeSpecBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(TypeName.get(interfaceElement.asType()))
                .addField(ScaleReaderRegistry.class, SCALE_READER_REGISTRY, Modifier.PRIVATE, Modifier.FINAL)
                .addField(ScaleWriterRegistry.class, SCALE_WRITER_REGISTRY, Modifier.PRIVATE, Modifier.FINAL)
                .addField(State.class, STATE, Modifier.PRIVATE, Modifier.FINAL);

        val constructorBuilder = createConstructorBuilder();

        for (Element method : interfaceElement.getEnclosedElements()) {
            if (!(method instanceof ExecutableElement)) {
                continue;
            }

            this.methodProcessor.process(name,
                    (ExecutableElement) method,
                    typeSpecBuilder,
                    constructorBuilder,
                    context);
        }

        typeSpecBuilder.addMethod(constructorBuilder.build());

        JavaFile.builder(packageName, typeSpecBuilder.build()).build().writeTo(context.getFiler());
    }

    private MethodSpec.Builder createConstructorBuilder() {
        val ctor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ScaleReaderRegistry.class, SCALE_READER_REGISTRY)
                .addParameter(ScaleWriterRegistry.class, SCALE_WRITER_REGISTRY)
                .addParameter(State.class, STATE);

        Stream.of(SCALE_READER_REGISTRY, SCALE_WRITER_REGISTRY, STATE)
                .forEach(x ->
                        ctor.beginControlFlow("if ($L == null)", x)
                                .addStatement("throw new $T(\"$L can't be null.\")", IllegalArgumentException.class, x)
                                .endControlFlow()
                                .addStatement("this.$1L = $1L", x));

        return ctor;
    }
}
