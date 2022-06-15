package com.strategyobject.substrateclient.rpc.codegen.sections;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.strategyobject.substrateclient.common.codegen.ProcessingException;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import com.strategyobject.substrateclient.rpc.RpcDecoder;
import com.strategyobject.substrateclient.rpc.codegen.decoder.DecoderCompositor;
import com.strategyobject.substrateclient.rpc.codegen.encoder.EncoderCompositor;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.ScaleUtils;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleGeneric;
import com.strategyobject.substrateclient.scale.codegen.ScaleAnnotationParser;
import com.strategyobject.substrateclient.scale.codegen.reader.ReaderCompositor;
import com.strategyobject.substrateclient.scale.codegen.writer.WriterCompositor;
import lombok.NonNull;
import lombok.val;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static com.strategyobject.substrateclient.common.codegen.AnnotationUtils.suppressWarnings;
import static com.strategyobject.substrateclient.rpc.codegen.Constants.*;
import static com.strategyobject.substrateclient.rpc.codegen.sections.Constants.EMPTY_TYPE_VAR_MAP;

public abstract class RpcMethodProcessor<A extends Annotation> extends RpcInterfaceMethodProcessor {
    protected static final String PARAMS_VAR = "params";

    private final Class<A> processingAnnotation;

    protected RpcMethodProcessor(@NonNull Class<A> clazz, @NonNull TypeElement interfaceElement) {
        super(interfaceElement);

        processingAnnotation = clazz;
    }

    @Override
    void process(@NonNull String section,
                 @NonNull ExecutableElement method,
                 TypeSpec.@NonNull Builder typeSpecBuilder,
                 @NonNull ProcessorContext context) throws ProcessingException {
        onProcessStarting(context);

        val annotation = method.getAnnotation(processingAnnotation);
        if (annotation == null) {
            return;
        }

        ensureAnnotationIsFilled(method, annotation);
        typeSpecBuilder.addMethod(createMethod(section, method, annotation, context));
    }

    protected abstract void onProcessStarting(ProcessorContext context);

    private MethodSpec createMethod(String section, ExecutableElement method, A annotation, ProcessorContext context) throws ProcessingException {
        val returnType = method.getReturnType();
        ensureMethodHasAppropriateReturnType(method, returnType, context);

        val methodSpecBuilder = MethodSpec.methodBuilder(method.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addAnnotation(suppressWarnings("unchecked"))
                .returns(TypeName.get(returnType));

        val scaleAnnotationParser = new ScaleAnnotationParser(context);

        processParameters(methodSpecBuilder, method, scaleAnnotationParser, context);

        sendRequest(methodSpecBuilder, method, section, annotation, scaleAnnotationParser, context);

        return methodSpecBuilder.build();
    }

    private void sendRequest(MethodSpec.Builder methodSpecBuilder,
                             ExecutableElement method,
                             String section,
                             A annotation,
                             ScaleAnnotationParser scaleAnnotationParser,
                             ProcessorContext context) {
        callProviderInterface(methodSpecBuilder,
                method,
                section,
                annotation,
                context,
                (resultType, arg) -> getDecodeCodeBlock(method, resultType, arg, scaleAnnotationParser, context));
    }

    protected abstract void callProviderInterface(MethodSpec.Builder methodSpecBuilder,
                                                  ExecutableElement method,
                                                  String section,
                                                  A annotation,
                                                  ProcessorContext context,
                                                  BiFunction<TypeMirror, String, CodeBlock> decoder);

    private CodeBlock getDecodeCodeBlock(AnnotatedConstruct annotated,
                                         TypeMirror resultType,
                                         String arg,
                                         ScaleAnnotationParser scaleAnnotationParser,
                                         ProcessorContext context) {
        return annotated.getAnnotation(Scale.class) != null || annotated.getAnnotation(ScaleGeneric.class) != null ?
                getScaleReadCodeBlock(annotated, resultType, arg, scaleAnnotationParser, context) :
                getRpcDecodeCodeBlock(resultType, arg, context);
    }

    private CodeBlock getRpcDecodeCodeBlock(TypeMirror resultType, String arg, ProcessorContext context) {
        val decoderCompositor = new DecoderCompositor(
                context,
                EMPTY_TYPE_VAR_MAP,
                String.format("%s[$L].%s", DECODERS_ARG, DECODER_UNSAFE_ACCESSOR),
                String.format("%s[$L].%s", DECODERS_ARG, READER_UNSAFE_ACCESSOR),
                READER_UNSAFE_ACCESSOR);

        val decodeCode = decoderCompositor.traverse(resultType);

        return CodeBlock.builder()
                .add("($T) (($T)", resultType, RpcDecoder.class)
                .add(decodeCode)
                .add(".$L)", DECODER_ACCESSOR)
                .add(".$L($L)", DECODE_METHOD_NAME, arg)
                .build();
    }

    private CodeBlock getScaleReadCodeBlock(AnnotatedConstruct annotated,
                                            TypeMirror resultType,
                                            String arg,
                                            ScaleAnnotationParser scaleAnnotationParser,
                                            ProcessorContext context) {
        val readerCompositor = ReaderCompositor.disallowOpenGeneric(context);
        val typeOverride = scaleAnnotationParser.parse(annotated);
        val readerCode = typeOverride != null ?
                readerCompositor.traverse(resultType, typeOverride) :
                readerCompositor.traverse(resultType);

        return CodeBlock.builder()
                .add("($T) (", resultType)
                .add("$T.$L(", ScaleUtils.class, FROM_HEX_STRING)
                .add("$L.asString(), ($T) ", arg, ScaleReader.class)
                .add(readerCode)
                .add("))")
                .build();
    }

    private void processParameters(MethodSpec.Builder methodSpecBuilder,
                                   ExecutableElement method,
                                   ScaleAnnotationParser scaleAnnotationParser,
                                   ProcessorContext context) throws ProcessingException {
        if (method.getParameters().isEmpty()) {
            onParametersVisited(method);

            return;
        }

        val writerCompositor = WriterCompositor.disallowOpenGeneric(context);
        val encoderCompositor = new EncoderCompositor(
                context,
                EMPTY_TYPE_VAR_MAP,
                String.format("%s[$L].%s", ENCODERS_ARG, ENCODER_UNSAFE_ACCESSOR),
                String.format("%s[$L].%s", ENCODERS_ARG, WRITER_UNSAFE_ACCESSOR),
                WRITER_UNSAFE_ACCESSOR
        );

        methodSpecBuilder
                .addStatement("$1T<$2T> $3L = new $4T<$2T>()",
                        List.class,
                        Object.class,
                        PARAMS_VAR,
                        ArrayList.class);

        for (val param : method.getParameters()) {
            try {
                processParameter(methodSpecBuilder, method, param, encoderCompositor, writerCompositor, scaleAnnotationParser, context);
            } catch (Exception e) {
                throw new ProcessingException(e, param, e.getMessage());
            }
        }

        onParametersVisited(method);
    }

    private void processParameter(MethodSpec.Builder methodSpecBuilder,
                                  ExecutableElement method,
                                  VariableElement param,
                                  EncoderCompositor encoderCompositor,
                                  WriterCompositor writerCompositor,
                                  ScaleAnnotationParser scaleAnnotationParser,
                                  ProcessorContext context) throws ProcessingException {
        methodSpecBuilder
                .addParameter(TypeName.get(param.asType()), param.getSimpleName().toString());

        if (shouldBePassedToProvider(method, param, context)) {
            CodeBlock encodeBlock;
            if (param.getAnnotation(Scale.class) != null || param.getAnnotation(ScaleGeneric.class) != null) {
                encodeBlock = getScaleWriteCodeBlock(param, scaleAnnotationParser, writerCompositor);
            } else {
                encodeBlock = getRpcEncodeCodeBlock(param, encoderCompositor);
            }

            methodSpecBuilder
                    .addStatement(encodeBlock);
        }
    }

    private CodeBlock getScaleWriteCodeBlock(VariableElement param,
                                             ScaleAnnotationParser scaleAnnotationParser,
                                             WriterCompositor writerCompositor) {
        val type = param.asType();
        val typeOverride = scaleAnnotationParser.parse(param);
        val writerCode = typeOverride != null ?
                writerCompositor.traverse(type, typeOverride) :
                writerCompositor.traverse(type);

        return CodeBlock.builder()
                .add("params.add(")
                .add("$T.$L($L, ($T)", ScaleUtils.class, TO_HEX, param, ScaleWriter.class)
                .add(writerCode)
                .add("))")
                .build();
    }

    private CodeBlock getRpcEncodeCodeBlock(VariableElement param,
                                            EncoderCompositor encoderCompositor) {
        val encoderCode = encoderCompositor.traverse(param.asType());

        return CodeBlock.builder()
                .add("params.add(")
                .add(encoderCode)
                .add(".$L", ENCODER_ACCESSOR)
                .add(".$L($L))", ENCODE_METHOD_NAME, param)
                .build();
    }

    protected abstract boolean shouldBePassedToProvider(ExecutableElement method, VariableElement param, ProcessorContext context) throws ProcessingException;

    protected abstract void onParametersVisited(ExecutableElement method) throws ProcessingException;

    protected abstract void ensureMethodHasAppropriateReturnType(ExecutableElement method, TypeMirror returnType, ProcessorContext context) throws ProcessingException;

    protected abstract void ensureAnnotationIsFilled(ExecutableElement method, A annotation) throws ProcessingException;
}
