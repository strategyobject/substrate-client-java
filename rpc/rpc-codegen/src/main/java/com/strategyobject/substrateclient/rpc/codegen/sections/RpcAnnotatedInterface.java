package com.strategyobject.substrateclient.rpc.codegen.sections;

import com.google.common.base.Strings;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.strategyobject.substrateclient.common.codegen.ProcessingException;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcInterface;
import com.strategyobject.substrateclient.transport.ProviderInterface;
import lombok.NonNull;
import lombok.val;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;

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

        if (Strings.isNullOrEmpty(section = annotation.section())) {
            throw new ProcessingException(
                    interfaceElement,
                    "`@%s` of `%s` contains null or empty `section`.",
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
                .addMethod(createConstructor());

        for (val method : interfaceElement.getEnclosedElements()) {
            methodProcessor.process(section, (ExecutableElement) method, typeSpecBuilder, context);
        }

        JavaFile.builder(packageName, typeSpecBuilder.build()).build().writeTo(context.getFiler());
    }

    private MethodSpec createConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ProviderInterface.class, PROVIDER_INTERFACE)
                .beginControlFlow("if ($L == null)", PROVIDER_INTERFACE)
                .addStatement("throw new $T(\"$L can't be null.\")", IllegalArgumentException.class, PROVIDER_INTERFACE)
                .endControlFlow()
                .addStatement("this.$1L = $1L", PROVIDER_INTERFACE)
                .build();
    }
}
