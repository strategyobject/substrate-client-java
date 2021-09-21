package com.strategyobject.substrateclient.scale.codegen.reader;

import com.squareup.javapoet.*;
import com.strategyobject.substrateclient.common.codegen.JavaPoet;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.annotations.AutoRegister;
import com.strategyobject.substrateclient.scale.annotations.Ignore;
import com.strategyobject.substrateclient.scale.codegen.ProcessingException;
import com.strategyobject.substrateclient.scale.codegen.ProcessorContext;
import com.strategyobject.substrateclient.scale.codegen.ScaleAnnotationParser;
import com.strategyobject.substrateclient.scale.registry.ScaleReaderRegistry;
import lombok.NonNull;
import lombok.val;
import lombok.var;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

public class ScaleReaderAnnotatedClass {

    private final TypeElement classElement;
    private final Map<String, Integer> typeVarMap;

    public ScaleReaderAnnotatedClass(TypeElement classElement) {
        this.classElement = classElement;
        val typeParameters = classElement.getTypeParameters();
        this.typeVarMap = IntStream.range(0, typeParameters.size())
                .boxed()
                .collect(toMap(i -> typeParameters.get(i).toString(), Function.identity()));
    }

    public void generateReader(@NonNull ProcessorContext context,
                               @NonNull Filer filer) throws IOException, ProcessingException {
        val className = classElement.getSimpleName().toString();
        val readerName = context.getReaderName(className);
        val classWildcardTyped = JavaPoet.setEachGenericParameterAsWildcard(classElement);

        val typeSpecBuilder = TypeSpec.classBuilder(readerName)
                .addAnnotation(AnnotationSpec.builder(AutoRegister.class)
                        .addMember("types", "{$L.class}", className)
                        .build())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(ScaleReader.class), classWildcardTyped))
                .addMethod(generateReadMethod(classWildcardTyped, context));

        JavaFile.builder(
                context.getPackageName(classElement),
                typeSpecBuilder.build()
        ).build().writeTo(filer);
    }

    private MethodSpec generateReadMethod(TypeName classWildcardTyped,
                                          ProcessorContext context) throws ProcessingException {
        val methodSpec = MethodSpec.methodBuilder("read")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(classWildcardTyped)
                .addParameter(InputStream.class, "stream")
                .addParameter(ArrayTypeName.of(
                                ParameterizedTypeName.get(
                                        ClassName.get(ScaleReader.class),
                                        WildcardTypeName.subtypeOf(Object.class))),
                        "readers")
                .varargs(true)
                .addException(IOException.class);

        addValidationRules(methodSpec);
        addMethodBody(methodSpec, context);
        return methodSpec.build();
    }

    private void addValidationRules(MethodSpec.Builder methodSpec) {
        methodSpec.addStatement("if (stream == null) throw new IllegalArgumentException(\"stream is null\")");

        val classTypeParametersSize = classElement.getTypeParameters().size();
        if (classTypeParametersSize == 0) {
            methodSpec.addStatement("if (readers != null && readers.length > 0) throw new IllegalArgumentException()");
        } else {
            methodSpec
                    .addStatement("if (readers == null) throw new IllegalArgumentException(\"readers is null\")")
                    .addStatement("if (readers.length != $L) throw new IllegalArgumentException()", classTypeParametersSize);
            for (var i = 0; i < classTypeParametersSize; i++) {
                methodSpec.addStatement("if (readers[$L] == null) throw new NullPointerException()", i);
            }
        }
    }

    private void addMethodBody(MethodSpec.Builder methodSpec,
                               ProcessorContext context) throws ProcessingException {
        val resultType = JavaPoet.setEachGenericParameterAs(classElement, TypeName.OBJECT);
        methodSpec
                .addStatement("$1T registry = $1T.getInstance()", ScaleReaderRegistry.class)
                .addStatement("$1T result = new $1T()", resultType)
                .beginControlFlow("try");

        val scaleAnnotationParser = new ScaleAnnotationParser(context);
        val generator = new TypeReadGenerator(CodeBlock.class, context, typeVarMap);
        for (Element element : classElement.getEnclosedElements()) {
            if (element instanceof VariableElement) {
                val field = (VariableElement) element;
                if (field.getAnnotation(Ignore.class) == null) {
                    setField(methodSpec, field, context.getTypeUtils(), scaleAnnotationParser, generator);
                }
            }
        }

        methodSpec
                .nextControlFlow("catch ($T e)", Exception.class)
                .addStatement("throw new $T(e)", RuntimeException.class)
                .endControlFlow()
                .addStatement("return result");
    }

    private void setField(MethodSpec.Builder methodSpec,
                          VariableElement field,
                          Types typeUtils,
                          ScaleAnnotationParser scaleAnnotationParser,
                          TypeReadGenerator generator) throws ProcessingException {
        try {
            val fieldType = field.asType();
            val code = CodeBlock.builder();
            if (fieldType instanceof TypeVariable) {
                code.add("result.$L = ", field);
            } else {
                code.add("result.$L = ($T) ", field, typeUtils.erasure(fieldType));
            }

            val typeOverride = scaleAnnotationParser.parse(field);
            val readerCode = typeOverride != null ?
                    generator.traverse(fieldType, typeOverride) :
                    generator.traverse(fieldType);
            methodSpec.addStatement(code.add(readerCode).add(".read(stream)").build());
        } catch (Exception e) {
            throw new ProcessingException(e, field, e.getMessage());
        }
    }
}
