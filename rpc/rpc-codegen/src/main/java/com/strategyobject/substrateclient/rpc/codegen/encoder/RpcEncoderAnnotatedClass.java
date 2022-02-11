package com.strategyobject.substrateclient.rpc.codegen.encoder;

import com.squareup.javapoet.*;
import com.strategyobject.substrateclient.common.codegen.JavaPoet;
import com.strategyobject.substrateclient.common.codegen.ProcessingException;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import com.strategyobject.substrateclient.rpc.core.EncoderPair;
import com.strategyobject.substrateclient.rpc.core.RpcEncoder;
import com.strategyobject.substrateclient.rpc.core.annotations.AutoRegister;
import com.strategyobject.substrateclient.rpc.core.annotations.Ignore;
import com.strategyobject.substrateclient.rpc.core.registries.RpcEncoderRegistry;
import com.strategyobject.substrateclient.scale.ScaleUtils;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.annotations.Scale;
import com.strategyobject.substrateclient.scale.annotations.ScaleGeneric;
import com.strategyobject.substrateclient.scale.codegen.ScaleAnnotationParser;
import com.strategyobject.substrateclient.scale.codegen.writer.WriterCompositor;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.NonNull;
import lombok.val;
import lombok.var;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.strategyobject.substrateclient.common.codegen.AnnotationUtils.suppressWarnings;
import static com.strategyobject.substrateclient.common.codegen.TypeUtils.getGetterName;
import static com.strategyobject.substrateclient.rpc.codegen.Constants.*;
import static java.util.stream.Collectors.toMap;

public class RpcEncoderAnnotatedClass {
    private static final String ENCODER_NAME_TEMPLATE = "%sEncoder";
    private static final String SOURCE_ARG = "source";
    private static final String RESULT_VAR = "result";
    private final TypeElement classElement;
    private final Map<String, Integer> typeVarMap;
    private final List<VariableElement> fields;

    public RpcEncoderAnnotatedClass(@NonNull TypeElement classElement) {
        this.classElement = classElement;
        val typeParameters = classElement.getTypeParameters();
        this.typeVarMap = IntStream.range(0, typeParameters.size())
                .boxed()
                .collect(toMap(i -> typeParameters.get(i).toString(), Function.identity()));
        fields = classElement.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.FIELD)
                .map(e -> (VariableElement) e)
                .collect(Collectors.toList());
    }

    public void generateEncoder(@NonNull ProcessorContext context) throws ProcessingException, IOException {
        val encoder = String.format(ENCODER_NAME_TEMPLATE, classElement.getSimpleName().toString());
        val classWildcardTyped = JavaPoet.setEachGenericParameterAsWildcard(classElement);

        val typeSpecBuilder = TypeSpec.classBuilder(encoder)
                .addAnnotation(AnnotationSpec.builder(AutoRegister.class)
                        .addMember(AUTO_REGISTER_TYPES_ARG, "{$L.class}", classElement.getQualifiedName().toString())
                        .build())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(RpcEncoder.class), classWildcardTyped))
                .addMethod(generateEncodeMethod(context, classWildcardTyped));

        JavaFile.builder(
                context.getPackageName(classElement),
                typeSpecBuilder.build()
        ).build().writeTo(context.getFiler());
    }

    private MethodSpec generateEncodeMethod(ProcessorContext context, TypeName classWildcardTyped) throws ProcessingException {
        val methodSpec = MethodSpec.methodBuilder(ENCODE_METHOD_NAME)
                .addAnnotation(Override.class)
                .addAnnotation(suppressWarnings("unchecked"))
                .addModifiers(Modifier.PUBLIC)
                .returns(Object.class)
                .addParameter(classWildcardTyped, SOURCE_ARG)
                .addParameter(ArrayTypeName.of(
                                ParameterizedTypeName.get(
                                        ClassName.get(EncoderPair.class),
                                        WildcardTypeName.subtypeOf(Object.class))),
                        ENCODERS_ARG)
                .varargs(true);

        shortcutIfNull(methodSpec);
        addValidationRules(methodSpec, context);
        addMethodBody(methodSpec, context);

        return methodSpec.build();
    }

    private void addMethodBody(MethodSpec.Builder methodSpec, ProcessorContext context) throws ProcessingException {
        methodSpec
                .addStatement("$1T $2L = $1T.getInstance()", RpcEncoderRegistry.class, ENCODER_REGISTRY)
                .addStatement("$1T $2L = $1T.getInstance()", ScaleWriterRegistry.class, SCALE_WRITER_REGISTRY)
                .addStatement("$1T<$2T, $3T> $4L = new $1T<>()", HashMap.class, String.class, Object.class, RESULT_VAR)
                .beginControlFlow("try");

        setFields(methodSpec, context);

        methodSpec
                .nextControlFlow("catch ($T e)", Exception.class)
                .addStatement("throw new $T(e)", RuntimeException.class)
                .endControlFlow()
                .addStatement("return $L", RESULT_VAR);
    }

    private void setFields(MethodSpec.Builder methodSpec, ProcessorContext context) throws ProcessingException {
        val encoderCompositor = new EncoderCompositor(context,
                typeVarMap,
                String.format("%s[$L].%s", ENCODERS_ARG, ENCODER_UNSAFE_ACCESSOR),
                String.format("%s[$L].%s", ENCODERS_ARG, WRITER_UNSAFE_ACCESSOR),
                WRITER_UNSAFE_ACCESSOR,
                ENCODER_REGISTRY,
                SCALE_WRITER_REGISTRY);
        val scaleAnnotationParser = new ScaleAnnotationParser(context);
        val scaleWriterCompositor = new WriterCompositor(context,
                typeVarMap,
                String.format("%s[$L].%s", ENCODERS_ARG, WRITER_ACCESSOR),
                SCALE_WRITER_REGISTRY);

        for (VariableElement field : fields) {
            if (field.getAnnotation(Ignore.class) != null) {
                continue;
            }

            if (field.getAnnotation(Scale.class) != null || field.getAnnotation(ScaleGeneric.class) != null) {
                setScaleField(methodSpec, field, scaleAnnotationParser, scaleWriterCompositor);
            } else {
                setField(methodSpec, field, context, encoderCompositor);
            }
        }
    }

    private void setField(MethodSpec.Builder methodSpec,
                          VariableElement field,
                          ProcessorContext context,
                          EncoderCompositor encoderCompositor) throws ProcessingException {
        try {
            val fieldType = field.asType();
            val encoderCode = encoderCompositor.traverse(fieldType);

            methodSpec.addStatement(CodeBlock.builder()
                    .add("$1T $2L = ($1T)(($3T)(", getNonVariableType(fieldType, context), field, RpcEncoder.class)
                    .add(encoderCode)
                    .add(".$L", ENCODER_ACCESSOR)
                    .add(")).$L($L.$L())", ENCODE_METHOD_NAME, SOURCE_ARG, getGetterName(field))
                    .build());
            if (fieldType instanceof PrimitiveType) {
                methodSpec.addStatement("$1L.put($2S, $2L)", RESULT_VAR, field);
            } else {
                methodSpec.addStatement("if ($1L != null) $2L.put($1S, $1L)", field, RESULT_VAR);
            }
        } catch (Exception e) {
            throw new ProcessingException(e, field, e.getMessage());
        }
    }

    private TypeMirror getNonVariableType(TypeMirror fieldType, ProcessorContext context) {
        if (fieldType instanceof TypeVariable) {
            return context.getType(Object.class);
        }

        return context.getBoxed(context.erasure(fieldType));
    }

    private void setScaleField(MethodSpec.Builder methodSpec,
                               VariableElement field,
                               ScaleAnnotationParser scaleAnnotationParser,
                               WriterCompositor writerCompositor) throws ProcessingException {
        try {
            val fieldType = field.asType();
            val typeOverride = scaleAnnotationParser.parse(field);
            val writerCode = typeOverride != null ?
                    writerCompositor.traverse(fieldType, typeOverride) :
                    writerCompositor.traverse(fieldType);

            val getter = getGetterName(field);

            val code = CodeBlock.builder()
                    .add("$T $L = $T.$L($L.$L(), ($T)", String.class, field, ScaleUtils.class, TO_HEX, SOURCE_ARG, getter, ScaleWriter.class)
                    .add(writerCode)
                    .add(")")
                    .build();

            if (fieldType instanceof PrimitiveType) {
                methodSpec
                        .addStatement(code)
                        .addStatement("$1L.put($2S, $2L)", RESULT_VAR, field);
            } else {
                methodSpec
                        .beginControlFlow("if ($L.$L() != null)", SOURCE_ARG, getter)
                        .addStatement(code)
                        .addStatement("$1L.put($2S, $2L)", RESULT_VAR, field)
                        .endControlFlow();
            }
        } catch (Exception e) {
            throw new ProcessingException(e, field, e.getMessage());
        }

    }

    private void addValidationRules(MethodSpec.Builder methodSpec, ProcessorContext context) {
        val classTypeParametersSize = classElement.getTypeParameters().size();
        if (classTypeParametersSize == 0 || context.isSubtypeOf(classElement.asType(), context.erasure(context.getType(RPC_SELF_ENCODABLE)))) {
            methodSpec.addStatement("if ($1L != null && $1L.length > 0) throw new $2T()", ENCODERS_ARG, IllegalArgumentException.class);
        } else {
            methodSpec
                    .addStatement("if ($1L == null) throw new $2T(\"$1L is null\")", ENCODERS_ARG, IllegalArgumentException.class)
                    .addStatement("if ($L.length != $L) throw new $T()", ENCODERS_ARG, classTypeParametersSize, IllegalArgumentException.class);
            for (var i = 0; i < classTypeParametersSize; i++) {
                methodSpec.addStatement("if ($L[$L] == null) throw new $T()", ENCODERS_ARG, i, NullPointerException.class);
            }
        }
    }

    private void shortcutIfNull(MethodSpec.Builder methodSpec) {
        methodSpec.addStatement("if ($L == null) { return null; }", SOURCE_ARG);
    }
}
