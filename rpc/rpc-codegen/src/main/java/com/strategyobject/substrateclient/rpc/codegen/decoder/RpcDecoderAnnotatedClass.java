package com.strategyobject.substrateclient.rpc.codegen.decoder;

import com.squareup.javapoet.*;
import com.strategyobject.substrateclient.common.codegen.*;
import com.strategyobject.substrateclient.rpc.DecoderPair;
import com.strategyobject.substrateclient.rpc.RpcDecoder;
import com.strategyobject.substrateclient.rpc.annotation.AutoRegister;
import com.strategyobject.substrateclient.rpc.annotation.Ignore;
import com.strategyobject.substrateclient.rpc.registries.RpcDecoderRegistry;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.ScaleUtils;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleGeneric;
import com.strategyobject.substrateclient.scale.codegen.ScaleAnnotationParser;
import com.strategyobject.substrateclient.scale.codegen.reader.ReaderCompositor;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.transport.RpcObject;
import lombok.NonNull;
import lombok.val;
import lombok.var;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeVariable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.strategyobject.substrateclient.common.codegen.AnnotationUtils.suppressWarnings;
import static com.strategyobject.substrateclient.common.codegen.TypeUtils.getSetterName;
import static com.strategyobject.substrateclient.rpc.codegen.Constants.*;
import static java.util.stream.Collectors.toMap;

public class RpcDecoderAnnotatedClass {
    private static final String DECODER_NAME_TEMPLATE = "%sDecoder";
    private static final String VALUE_ARG = "value";
    private static final String RESULT_VAR = "result";
    private static final String MAP_VAR = "sourceMap";

    private final TypeElement classElement;
    private final Map<String, Integer> typeVarMap;
    private final List<VariableElement> fields;

    public RpcDecoderAnnotatedClass(@NonNull TypeElement classElement) {
        this.classElement = classElement;
        val typeParameters = classElement.getTypeParameters();
        this.typeVarMap = IntStream.range(0, typeParameters.size())
                .boxed()
                .collect(toMap(i -> typeParameters.get(i).toString(), Function.identity()));
        this.fields = classElement.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.FIELD)
                .map(VariableElement.class::cast)
                .collect(Collectors.toList());
    }

    public void generateDecoder(@NonNull ProcessorContext context) throws ProcessingException, IOException {
        val decoderName = String.format(DECODER_NAME_TEMPLATE, classElement.getSimpleName().toString());
        val classWildcardTyped = JavaPoet.setEachGenericParameterAsWildcard(classElement);

        val typeSpecBuilder = TypeSpec.classBuilder(decoderName)
                .addAnnotation(AnnotationSpec.builder(AutoRegister.class)
                        .addMember(AUTO_REGISTER_TYPES_ARG, "{$L.class}", classElement.getQualifiedName().toString())
                        .build())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(RpcDecoder.class), classWildcardTyped));

        injectDependencies(typeSpecBuilder)
                .addMethod(generateDecodeMethod(context, classWildcardTyped));

        JavaFile.builder(
                context.getPackageName(classElement),
                typeSpecBuilder.build()
        ).build().writeTo(context.getFiler());
    }

    private MethodSpec generateDecodeMethod(ProcessorContext context, TypeName classWildcardTyped) throws ProcessingException {
        val methodSpec = MethodSpec.methodBuilder(DECODE_METHOD_NAME)
                .addAnnotation(Override.class)
                .addAnnotation(suppressWarnings("unchecked"))
                .addModifiers(Modifier.PUBLIC)
                .returns(classWildcardTyped)
                .addParameter(RpcObject.class, VALUE_ARG)
                .addParameter(ArrayTypeName.of(
                                ParameterizedTypeName.get(
                                        ClassName.get(DecoderPair.class),
                                        WildcardTypeName.subtypeOf(Object.class))),
                        DECODERS_ARG)
                .varargs(true);

        shortcutIfNull(methodSpec);
        addValidationRules(methodSpec);
        addMethodBody(methodSpec, context);

        return methodSpec.build();
    }

    private TypeSpec.Builder injectDependencies(TypeSpec.Builder typeSpecBuilder) {
        val ctor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(RpcDecoderRegistry.class, DECODER_REGISTRY)
                .addParameter(ScaleReaderRegistry.class, SCALE_READER_REGISTRY)
                .beginControlFlow("if ($L == null)", DECODER_REGISTRY)
                .addStatement("throw new $T(\"$L can't be null.\")", IllegalArgumentException.class, DECODER_REGISTRY)
                .endControlFlow()
                .addStatement("this.$1L = $1L", DECODER_REGISTRY)
                .beginControlFlow("if ($L == null)", SCALE_READER_REGISTRY)
                .addStatement("throw new $T(\"$L can't be null.\")", IllegalArgumentException.class, SCALE_READER_REGISTRY)
                .endControlFlow()
                .addStatement("this.$1L = $1L", SCALE_READER_REGISTRY)
                .build();

        return typeSpecBuilder
                .addField(RpcDecoderRegistry.class, DECODER_REGISTRY, Modifier.PRIVATE, Modifier.FINAL)
                .addField(ScaleReaderRegistry.class, SCALE_READER_REGISTRY, Modifier.PRIVATE, Modifier.FINAL)
                .addMethod(ctor);
    }

    private void addMethodBody(MethodSpec.Builder methodSpec, ProcessorContext context) throws ProcessingException {
        val resultType = JavaPoet.setEachGenericParameterAs(classElement, TypeName.OBJECT);
        methodSpec
                .addStatement("$T<$T, $T> $L = $L.asMap()", Map.class, String.class, RpcObject.class, MAP_VAR, VALUE_ARG)
                .addStatement("$1T $2L = new $1T()", resultType, RESULT_VAR)
                .beginControlFlow("try");

        setFields(methodSpec, context);

        methodSpec
                .nextControlFlow("catch ($T e)", Exception.class)
                .addStatement("throw new $T(e)", RuntimeException.class)
                .endControlFlow()
                .addStatement("return $L", RESULT_VAR);
    }

    private void setFields(MethodSpec.Builder methodSpec, ProcessorContext context) throws ProcessingException {
        val decoderCompositor = new DecoderCompositor(
                context,
                typeVarMap,
                String.format("%s[$L].%s", DECODERS_ARG, DECODER_UNSAFE_ACCESSOR),
                String.format("%s[$L].%s", DECODERS_ARG, READER_UNSAFE_ACCESSOR),
                READER_UNSAFE_ACCESSOR,
                DECODER_REGISTRY,
                SCALE_READER_REGISTRY);
        val scaleAnnotationParser = new ScaleAnnotationParser(context);
        val scaleReaderCompositor = ReaderCompositor.forAnyType(
                context,
                typeVarMap,
                String.format("%s[$L].%s", DECODERS_ARG, READER_ACCESSOR),
                SCALE_READER_REGISTRY);

        for (VariableElement field : fields) {
            if (AnnotationUtils.isAnnotatedWith(field, Ignore.class)) {
                continue;
            }

            if (AnnotationUtils.isAnnotatedWithAny(field, Scale.class, ScaleGeneric.class)) {
                setScaleField(methodSpec, context, field, scaleAnnotationParser, scaleReaderCompositor);
            } else {
                setField(methodSpec, field, context, decoderCompositor);
            }
        }
    }

    private void setScaleField(MethodSpec.Builder methodSpec,
                               ProcessorContext context,
                               VariableElement field,
                               ScaleAnnotationParser scaleAnnotationParser,
                               ReaderCompositor readerCompositor) throws ProcessingException {
        try {
            val fieldType = field.asType();
            val code = CodeBlock.builder();
            if (fieldType instanceof TypeVariable) {
                code.add("$L.$L(", RESULT_VAR, getSetterName(field.getSimpleName().toString()));
            } else {
                code.add("$L.$L(($T)",
                        RESULT_VAR,
                        getSetterName(field.getSimpleName().toString()),
                        context.getBoxed(context.erasure(fieldType)));
            }

            val typeOverride = scaleAnnotationParser.parse(field);
            val readerCode = typeOverride != null ?
                    readerCompositor.traverse(fieldType, typeOverride) :
                    readerCompositor.traverse(fieldType);
            methodSpec
                    .addStatement(code
                            .add("$T.$L(", ScaleUtils.class, FROM_HEX_STRING)
                            .add("$L.get($S).asString(), ", MAP_VAR, field)
                            .add("($T)", ScaleReader.class)
                            .add(readerCode)
                            .add("))")
                            .build());
        } catch (Exception e) {
            throw new ProcessingException(e, field, e.getMessage());
        }
    }

    private void setField(MethodSpec.Builder methodSpec,
                          VariableElement field,
                          ProcessorContext context,
                          DecoderCompositor decoderCompositor) throws ProcessingException {
        try {
            val fieldType = field.asType();
            val code = CodeBlock.builder();
            if (fieldType instanceof TypeVariable) {
                code.add("$L.$L((", RESULT_VAR, getSetterName(field.getSimpleName().toString()));
            } else {
                code.add("$L.$L(($T)(",
                        RESULT_VAR,
                        getSetterName(field.getSimpleName().toString()),
                        context.getBoxed(context.erasure(fieldType)));
            }

            val decoderCode = decoderCompositor.traverse(fieldType);
            methodSpec.addStatement(code
                    .add(decoderCode)
                    .add(".$L", DECODER_ACCESSOR)
                    .add(".$L($L.get($S))))",
                            DECODE_METHOD_NAME,
                            MAP_VAR,
                            field)
                    .build());
        } catch (Exception e) {
            throw new ProcessingException(e, field, e.getMessage());
        }
    }

    private void addValidationRules(MethodSpec.Builder methodSpec) {
        val classTypeParametersSize = classElement.getTypeParameters().size();
        if (classTypeParametersSize == 0) {
            methodSpec.addStatement("if ($1L != null && $1L.length > 0) throw new $2T()", DECODERS_ARG, IllegalArgumentException.class);
        } else {
            methodSpec
                    .addStatement("if ($1L == null) throw new $2T(\"$1L is null\")", DECODERS_ARG, IllegalArgumentException.class)
                    .addStatement("if ($L.length != $L) throw new $T()", DECODERS_ARG, classTypeParametersSize, IllegalArgumentException.class);
            for (var i = 0; i < classTypeParametersSize; i++) {
                methodSpec.addStatement("if ($L[$L] == null) throw new $T()", DECODERS_ARG, i, NullPointerException.class);
            }
        }
    }

    private void shortcutIfNull(MethodSpec.Builder methodSpec) {
        methodSpec.addStatement("if ($L.isNull()) { return null; }", VALUE_ARG);
    }
}
