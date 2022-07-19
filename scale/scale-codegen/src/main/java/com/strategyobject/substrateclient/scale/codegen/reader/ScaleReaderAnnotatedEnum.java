package com.strategyobject.substrateclient.scale.codegen.reader;

import com.squareup.javapoet.*;
import com.strategyobject.substrateclient.common.codegen.ProcessingException;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.common.types.Enums;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import com.strategyobject.substrateclient.scale.codegen.ScaleProcessorHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public class ScaleReaderAnnotatedEnum {
    private static final String READERS_ARG = "readers";
    private static final String ENUM_VALUES = "values";

    private final @NonNull TypeElement enumElement;

    public void generateReader(@NonNull ProcessorContext context) throws IOException, ProcessingException {
        val readerName = ScaleProcessorHelper.getReaderName(enumElement.getSimpleName().toString());
        val enumType = TypeName.get(enumElement.asType());

        val typeSpecBuilder = TypeSpec.classBuilder(readerName)
                .addAnnotation(AnnotationSpec.builder(AutoRegister.class)
                        .addMember("types", "{$L.class}", enumElement.getQualifiedName().toString())
                        .build())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(ScaleReader.class), enumType))
                .addField(
                        FieldSpec.builder(ArrayTypeName.of(enumType), ENUM_VALUES, Modifier.PRIVATE, Modifier.FINAL)
                                .initializer("$T.values()", enumType)
                                .build())
                .addMethod(generateReadMethod(enumType));

        JavaFile.builder(
                context.getPackageName(enumElement),
                typeSpecBuilder.build()
        ).build().writeTo(context.getFiler());
    }

    private MethodSpec generateReadMethod(TypeName enumType) {
        val methodSpec = MethodSpec.methodBuilder("read")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(enumType)
                .addParameter(InputStream.class, "stream")
                .addParameter(ArrayTypeName.of(
                                ParameterizedTypeName.get(
                                        ClassName.get(ScaleReader.class),
                                        WildcardTypeName.subtypeOf(Object.class))),
                        READERS_ARG)
                .varargs(true)
                .addException(IOException.class);

        addValidationRules(methodSpec);
        addMethodBody(methodSpec);
        return methodSpec.build();
    }

    private void addValidationRules(MethodSpec.Builder methodSpec) {
        methodSpec
                .addStatement("if (stream == null) throw new IllegalArgumentException(\"stream is null\")")
                .addStatement("if (readers != null && readers.length > 0) throw new IllegalArgumentException()");
    }

    private void addMethodBody(MethodSpec.Builder methodSpec) {
        methodSpec.addStatement("return $T.lookup($L, $T.readByte(stream))", Enums.class, ENUM_VALUES, Streamer.class);
    }
}
