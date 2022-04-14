package com.strategyobject.substrateclient.scale.codegen.reader;

import com.squareup.javapoet.*;
import com.strategyobject.substrateclient.common.codegen.JavaPoet;
import com.strategyobject.substrateclient.common.codegen.ProcessingException;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.annotations.AutoRegister;
import com.strategyobject.substrateclient.scale.annotations.Ignore;
import com.strategyobject.substrateclient.scale.codegen.ScaleAnnotationParser;
import com.strategyobject.substrateclient.scale.codegen.ScaleProcessorHelper;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import lombok.NonNull;
import lombok.val;
import lombok.var;

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

import static com.strategyobject.substrateclient.common.codegen.AnnotationUtils.suppressWarnings;
import static com.strategyobject.substrateclient.common.codegen.TypeUtils.getSetterName;
import static java.util.stream.Collectors.toMap;

public class ScaleReaderAnnotatedClass {
    private static final String READERS_ARG = "readers";
    private static final String REGISTRY = "registry";

    private final TypeElement classElement;
    private final Map<String, Integer> typeVarMap;

    public ScaleReaderAnnotatedClass(@NonNull TypeElement classElement) {
        this.classElement = classElement;
        val typeParameters = classElement.getTypeParameters();
        this.typeVarMap = IntStream.range(0, typeParameters.size())
                .boxed()
                .collect(toMap(i -> typeParameters.get(i).toString(), Function.identity()));
    }

    public void generateReader(@NonNull ProcessorContext context) throws IOException, ProcessingException {
        val readerName = ScaleProcessorHelper.getReaderName(classElement.getSimpleName().toString());
        val classWildcardTyped = JavaPoet.setEachGenericParameterAsWildcard(classElement);

        val typeSpecBuilder = TypeSpec.classBuilder(readerName)
                .addAnnotation(AnnotationSpec.builder(AutoRegister.class)
                        .addMember("types", "{$L.class}", classElement.getQualifiedName().toString())
                        .build())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(ScaleReader.class), classWildcardTyped))
                .addMethod(generateReadMethod(classWildcardTyped, context));

        JavaFile.builder(
                context.getPackageName(classElement),
                typeSpecBuilder.build()
        ).build().writeTo(context.getFiler());
    }

    private MethodSpec generateReadMethod(TypeName classWildcardTyped,
                                          ProcessorContext context) throws ProcessingException {
        val methodSpec = MethodSpec.methodBuilder("read")
                .addAnnotation(Override.class)
                .addAnnotation(suppressWarnings("unchecked"))
                .addModifiers(Modifier.PUBLIC)
                .returns(classWildcardTyped)
                .addParameter(InputStream.class, "stream")
                .addParameter(ArrayTypeName.of(
                                ParameterizedTypeName.get(
                                        ClassName.get(ScaleReader.class),
                                        WildcardTypeName.subtypeOf(Object.class))),
                        READERS_ARG)
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
                .addStatement("$1T $2L = $1T.getInstance()", ScaleReaderRegistry.class, REGISTRY)
                .addStatement("$1T result = new $1T()", resultType)
                .beginControlFlow("try");

        val scaleAnnotationParser = new ScaleAnnotationParser(context);
        val compositor = ReaderCompositor.forAnyType(context,
                typeVarMap,
                String.format("%s[$L]", READERS_ARG),
                REGISTRY);
        for (Element element : classElement.getEnclosedElements()) {
            if (element instanceof VariableElement) {
                val field = (VariableElement) element;
                if (field.getAnnotation(Ignore.class) == null) {
                    setField(methodSpec, field, context.getTypeUtils(), scaleAnnotationParser, compositor);
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
                          ReaderCompositor compositor) throws ProcessingException {
        try {
            val fieldType = field.asType();
            val code = CodeBlock.builder();
            val setterName = getSetterName(field.getSimpleName().toString());
            if (fieldType instanceof TypeVariable) {
                code.add("result.$L(", setterName);
            } else {
                code.add("result.$L(($T) ", setterName, typeUtils.erasure(fieldType));
            }

            val typeOverride = scaleAnnotationParser.parse(field);
            val readerCode = typeOverride != null ?
                    compositor.traverse(fieldType, typeOverride) :
                    compositor.traverse(fieldType);
            methodSpec.addStatement(code.add(readerCode).add(".read(stream))").build());
        } catch (Exception e) {
            throw new ProcessingException(e, field, e.getMessage());
        }
    }
}
