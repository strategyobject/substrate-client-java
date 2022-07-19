package com.strategyobject.substrateclient.scale.codegen.writer;

import com.squareup.javapoet.*;
import com.strategyobject.substrateclient.common.codegen.ProcessingException;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import com.strategyobject.substrateclient.scale.codegen.ScaleProcessorHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.io.OutputStream;

@RequiredArgsConstructor
public class ScaleWriterAnnotatedEnum {
    private static final String WRITERS_ARG = "writers";

    private final @NonNull TypeElement enumElement;

    public void generateWriter(@NonNull ProcessorContext context) throws IOException, ProcessingException {
        val writerName = ScaleProcessorHelper.getWriterName(enumElement.getSimpleName().toString());
        val enumType = TypeName.get(enumElement.asType());

        val typeSpecBuilder = TypeSpec.classBuilder(writerName)
                .addAnnotation(AnnotationSpec.builder(AutoRegister.class)
                        .addMember("types", "{$L.class}", enumElement.getQualifiedName().toString())
                        .build())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(ScaleWriter.class), enumType))
                .addMethod(generateWriteMethod(enumType));

        JavaFile.builder(
                context.getPackageName(enumElement),
                typeSpecBuilder.build()
        ).build().writeTo(context.getFiler());
    }

    private MethodSpec generateWriteMethod(TypeName classWildcardTyped) {
        val methodSpec = MethodSpec.methodBuilder("write")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(classWildcardTyped, "value")
                .addParameter(OutputStream.class, "stream")
                .addParameter(ArrayTypeName.of(
                                ParameterizedTypeName.get(
                                        ClassName.get(ScaleWriter.class),
                                        WildcardTypeName.subtypeOf(Object.class))),
                        WRITERS_ARG)
                .varargs(true)
                .addException(IOException.class);

        addValidationRules(methodSpec);
        addMethodBody(methodSpec);
        return methodSpec.build();
    }

    private void addValidationRules(MethodSpec.Builder methodSpec) {
        methodSpec.addStatement("if (stream == null) throw new IllegalArgumentException(\"stream is null\")");
        methodSpec.addStatement("if (value == null) throw new IllegalArgumentException(\"value is null\")");
        methodSpec.addStatement("if (writers != null && writers.length > 0) throw new IllegalArgumentException()");
    }

    private void addMethodBody(MethodSpec.Builder methodSpec) {
        methodSpec.addStatement("stream.write(value.ordinal())");
    }
}
