package com.strategyobject.substrateclient.rpc.codegen;

import com.google.common.base.Strings;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.strategyobject.substrateclient.rpc.codegen.annotations.RpcInterface;
import com.strategyobject.substrateclient.rpc.core.ParameterConverter;
import com.strategyobject.substrateclient.rpc.core.ResultConverter;
import com.strategyobject.substrateclient.transport.ProviderInterface;
import lombok.NonNull;
import lombok.val;

import javax.annotation.processing.Filer;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;

import static com.strategyobject.substrateclient.rpc.codegen.Constants.CLASS_NAME_TEMPLATE;

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

    public void generateClass(@NonNull Types typeUtils, @NonNull Elements elementUtils, @NonNull Filer filer)
            throws ProcessingException, IOException {
        val interfaceName = interfaceElement.getSimpleName().toString();
        val className = String.format(CLASS_NAME_TEMPLATE, interfaceName);
        val pkg = elementUtils.getPackageOf(interfaceElement);
        val packageName = pkg.getQualifiedName().toString();

        val typeSpecBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(TypeName.get(interfaceElement.asType()))
                .addField(ProviderInterface.class, "providerInterface")
                .addField(ParameterConverter.class, "parameterConverter")
                .addField(ResultConverter.class, "resultConverter")
                .addMethod(createConstructor());

        for (val method : interfaceElement.getEnclosedElements()) {
            methodProcessor.process(section, (ExecutableElement) method, typeUtils, typeSpecBuilder, elementUtils);
        }

        JavaFile.builder(packageName, typeSpecBuilder.build()).build().writeTo(filer);
    }

    private MethodSpec createConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ProviderInterface.class, "providerInterface")
                .addParameter(ParameterConverter.class, "parameterConverter")
                .addParameter(ResultConverter.class, "resultConverter")
                .beginControlFlow("if (providerInterface == null)")
                .addStatement("throw new $T(\"ProviderInterface can't be null.\")", IllegalArgumentException.class)
                .endControlFlow()
                .beginControlFlow("if (parameterConverter == null)")
                .addStatement("throw new $T(\"ParameterConverter can't be null.\")", IllegalArgumentException.class)
                .endControlFlow()
                .beginControlFlow("if (resultConverter == null)")
                .addStatement("throw new $T(\"ResultConverter can't be null.\")", IllegalArgumentException.class)
                .endControlFlow()
                .addStatement("this.providerInterface = providerInterface")
                .addStatement("this.parameterConverter = parameterConverter")
                .addStatement("this.resultConverter = resultConverter")
                .build();
    }
}
