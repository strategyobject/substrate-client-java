package com.strategyobject.substrateclient.scale.codegen.writer;

import com.squareup.javapoet.*;
import com.strategyobject.substrateclient.common.codegen.AnnotationUtils;
import com.strategyobject.substrateclient.common.codegen.JavaPoet;
import com.strategyobject.substrateclient.common.codegen.ProcessingException;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import com.strategyobject.substrateclient.scale.annotation.Ignore;
import com.strategyobject.substrateclient.scale.codegen.ScaleAnnotationParser;
import com.strategyobject.substrateclient.scale.codegen.ScaleProcessorHelper;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.NonNull;
import lombok.val;
import lombok.var;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

import static com.strategyobject.substrateclient.common.codegen.AnnotationUtils.suppressWarnings;
import static com.strategyobject.substrateclient.common.codegen.TypeUtils.getGetterName;
import static java.util.stream.Collectors.toMap;

public class ScaleWriterAnnotatedClass {
    private static final String WRITERS_ARG = "writers";
    private static final String REGISTRY = "registry";

    private final TypeElement classElement;
    private final Map<String, Integer> typeVarMap;

    public ScaleWriterAnnotatedClass(TypeElement classElement) {
        this.classElement = classElement;
        val typeParameters = classElement.getTypeParameters();
        this.typeVarMap = IntStream.range(0, typeParameters.size())
                .boxed()
                .collect(toMap(i -> typeParameters.get(i).toString(), Function.identity()));
    }

    public void generateWriter(@NonNull ProcessorContext context) throws IOException, ProcessingException {
        val writerName = ScaleProcessorHelper.getWriterName(classElement.getSimpleName().toString());
        val classWildcardTyped = JavaPoet.setEachGenericParameterAsWildcard(classElement);

        val typeSpecBuilder = TypeSpec.classBuilder(writerName)
                .addAnnotation(AnnotationSpec.builder(AutoRegister.class)
                        .addMember("types", "{$L.class}", classElement.getQualifiedName().toString())
                        .build())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(ScaleWriter.class), classWildcardTyped));

        injectDependencies(typeSpecBuilder)
                .addMethod(generateWriteMethod(classWildcardTyped, context));

        JavaFile.builder(
                context.getPackageName(classElement),
                typeSpecBuilder.build()
        ).build().writeTo(context.getFiler());
    }

    private TypeSpec.Builder injectDependencies(TypeSpec.Builder typeSpecBuilder) {
        val ctor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ScaleWriterRegistry.class, REGISTRY)
                .beginControlFlow("if ($L == null)", REGISTRY)
                .addStatement("throw new $T(\"$L can't be null.\")", IllegalArgumentException.class, REGISTRY)
                .endControlFlow()
                .addStatement("this.$1L = $1L", REGISTRY)
                .build();

        return typeSpecBuilder
                .addField(ScaleWriterRegistry.class, REGISTRY, Modifier.PRIVATE, Modifier.FINAL)
                .addMethod(ctor);
    }

    private MethodSpec generateWriteMethod(TypeName classWildcardTyped,
                                           ProcessorContext context) throws ProcessingException {
        val methodSpec = MethodSpec.methodBuilder("write")
                .addAnnotation(Override.class)
                .addAnnotation(suppressWarnings("unchecked"))
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
        addMethodBody(methodSpec, context);
        return methodSpec.build();
    }

    private void addValidationRules(MethodSpec.Builder methodSpec) {
        methodSpec.addStatement("if (stream == null) throw new IllegalArgumentException(\"stream is null\")");
        methodSpec.addStatement("if (value == null) throw new IllegalArgumentException(\"value is null\")");

        val classTypeParametersSize = classElement.getTypeParameters().size();
        if (classTypeParametersSize == 0) {
            methodSpec.addStatement("if (writers != null && writers.length > 0) throw new IllegalArgumentException()");
        } else {
            methodSpec
                    .addStatement("if (writers == null) throw new IllegalArgumentException(\"writers is null\")")
                    .addStatement("if (writers.length != $L) throw new IllegalArgumentException()", classTypeParametersSize);
            for (var i = 0; i < classTypeParametersSize; i++) {
                methodSpec.addStatement("if (writers[$L] == null) throw new NullPointerException()", i);
            }
        }
    }

    private void addMethodBody(MethodSpec.Builder methodSpec,
                               ProcessorContext context) throws ProcessingException {
        methodSpec
                .beginControlFlow("try");

        val scaleAnnotationParser = new ScaleAnnotationParser(context);
        val compositor = WriterCompositor.forAnyType(context,
                typeVarMap,
                String.format("%s[$L]", WRITERS_ARG),
                REGISTRY);
        for (Element element : classElement.getEnclosedElements()) {
            if (element instanceof VariableElement) {
                val field = (VariableElement) element;
                if (!AnnotationUtils.isAnnotatedWith(field, Ignore.class) && !field.getModifiers().contains(Modifier.STATIC)) {
                    setField(methodSpec, field, scaleAnnotationParser, compositor);
                }
            }
        }

        methodSpec
                .nextControlFlow("catch ($T e)", Exception.class)
                .addStatement("throw new $T(e)", RuntimeException.class)
                .endControlFlow();
    }

    private void setField(MethodSpec.Builder methodSpec,
                          VariableElement field,
                          ScaleAnnotationParser scaleAnnotationParser,
                          WriterCompositor compositor) throws ProcessingException {
        try {
            val fieldType = field.asType();
            val typeOverride = scaleAnnotationParser.parse(field);
            val writerCode = typeOverride != null ?
                    compositor.traverse(fieldType, typeOverride) :
                    compositor.traverse(fieldType);

            methodSpec.addStatement(
                    CodeBlock.builder()
                            .add("(($T)", ScaleWriter.class)
                            .add(writerCode)
                            .add(").write(value.$L(), stream)", getGetterName(field))
                            .build());
        } catch (Exception e) {
            throw new ProcessingException(e, field, e.getMessage());
        }
    }
}
