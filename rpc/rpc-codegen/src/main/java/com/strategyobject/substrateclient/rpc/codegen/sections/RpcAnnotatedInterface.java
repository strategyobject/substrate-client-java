package com.strategyobject.substrateclient.rpc.codegen.sections;

import com.google.common.base.Strings;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.strategyobject.substrateclient.common.codegen.ProcessingException;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import com.strategyobject.substrateclient.rpc.annotation.RpcInterface;
import com.strategyobject.substrateclient.rpc.registries.RpcDecoderRegistry;
import com.strategyobject.substrateclient.rpc.registries.RpcEncoderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import com.strategyobject.substrateclient.transport.ProviderInterface;
import lombok.NonNull;
import lombok.val;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;

import static com.strategyobject.substrateclient.rpc.codegen.Constants.*;
import static com.strategyobject.substrateclient.rpc.codegen.sections.Constants.CLASS_NAME_TEMPLATE;
import static com.strategyobject.substrateclient.rpc.codegen.sections.Constants.PROVIDER_INTERFACE;

public class RpcAnnotatedInterface {
    private final TypeElement interfaceElement;
    private final String section;
    private final RpcInterfaceMethodProcessor methodProcessor;

    public RpcAnnotatedInterface(@NonNull TypeElement interfaceElement,
                                 @NonNull RpcInterfaceMethodProcessor methodProcessor) throws ProcessingException {
        this.interfaceElement = interfaceElement;
        val annotation = interfaceElement.getAnnotation(RpcInterface.class);

        if (!interfaceElement.getModifiers().contains(Modifier.PUBLIC)) {
            throw new ProcessingException(
                    interfaceElement,
                    "`%s` is not public. That is not allowed.",
                    interfaceElement.getQualifiedName().toString());
        }

        if (Strings.isNullOrEmpty(section = annotation.value())) {
            throw new ProcessingException(
                    interfaceElement,
                    "`@%s` of `%s` contains null or empty `value`.",
                    annotation.getClass().getSimpleName(),
                    interfaceElement.getQualifiedName().toString());
        }

        this.methodProcessor = methodProcessor;
    }

    public void generateClass(@NonNull ProcessorContext context)
            throws ProcessingException, IOException {
        val interfaceName = interfaceElement.getSimpleName().toString();
        val className = String.format(CLASS_NAME_TEMPLATE, interfaceName);
        val packageName = context.getPackageName(interfaceElement);

        val typeSpecBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(TypeName.get(interfaceElement.asType()))
                .addField(ProviderInterface.class, PROVIDER_INTERFACE)
                .addField(RpcEncoderRegistry.class, ENCODER_REGISTRY)
                .addField(ScaleWriterRegistry.class, SCALE_WRITER_REGISTRY)
                .addField(RpcDecoderRegistry.class, DECODER_REGISTRY)
                .addField(ScaleReaderRegistry.class, SCALE_READER_REGISTRY)
                .addMethod(createConstructor());

        for (Element method : interfaceElement.getEnclosedElements()) {
            methodProcessor.process(section, (ExecutableElement) method, typeSpecBuilder, context);
        }

        JavaFile.builder(packageName, typeSpecBuilder.build()).build().writeTo(context.getFiler());
    }

    private MethodSpec createConstructor() {
        val methodSpec = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ProviderInterface.class, PROVIDER_INTERFACE)
                .addParameter(RpcEncoderRegistry.class, ENCODER_REGISTRY)
                .addParameter(ScaleWriterRegistry.class, SCALE_WRITER_REGISTRY)
                .addParameter(RpcDecoderRegistry.class, DECODER_REGISTRY)
                .addParameter(ScaleReaderRegistry.class, SCALE_READER_REGISTRY);

        assignParameters(methodSpec,
                PROVIDER_INTERFACE,
                ENCODER_REGISTRY,
                SCALE_WRITER_REGISTRY,
                DECODER_REGISTRY,
                SCALE_READER_REGISTRY);

        return methodSpec.build();
    }

    private void assignParameters(MethodSpec.Builder methodSpec, String... parameters) {
        for (val parameter : parameters) {
            methodSpec
                    .beginControlFlow("if ($L == null)", parameter)
                    .addStatement("throw new $T(\"$L can't be null.\")", IllegalArgumentException.class, parameter)
                    .endControlFlow()
                    .addStatement("this.$1L = $1L", parameter);
        }
    }
}
