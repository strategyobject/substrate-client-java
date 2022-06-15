package com.strategyobject.substrateclient.rpc.codegen.sections;

import com.google.common.base.Strings;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.strategyobject.substrateclient.common.codegen.ProcessingException;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import com.strategyobject.substrateclient.rpc.annotation.RpcCall;
import lombok.NonNull;
import lombok.val;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

import static com.strategyobject.substrateclient.rpc.codegen.sections.Constants.*;

class RpcCallProcessor extends RpcMethodProcessor<RpcCall> {
    private static final String CALL_BACK_ARG = "r";

    public RpcCallProcessor(@NonNull TypeElement interfaceElement) {
        super(RpcCall.class, interfaceElement);
    }

    @Override
    protected void ensureAnnotationIsFilled(ExecutableElement method, RpcCall methodAnnotation)
            throws ProcessingException {
        if (Strings.isNullOrEmpty(methodAnnotation.value())) {
            throw new ProcessingException(
                    interfaceElement,
                    "`@%s` of `%s.%s` contains null or empty `value`.",
                    methodAnnotation.getClass().getSimpleName(),
                    interfaceElement.getQualifiedName().toString(),
                    method.getSimpleName());
        }
    }

    @Override
    protected void onProcessStarting(ProcessorContext context) {
        // This should be empty
    }

    @Override
    protected void callProviderInterface(MethodSpec.Builder methodSpecBuilder,
                                         ExecutableElement method,
                                         String section,
                                         RpcCall annotation,
                                         ProcessorContext context,
                                         BiFunction<TypeMirror, String, CodeBlock> decoder) {
        val rpcMethodName = String.format(RPC_METHOD_NAME_TEMPLATE, section, annotation.value());
        val paramsArgument = method.getParameters().isEmpty() ? "" : String.format(", %s", PARAMS_VAR);

        val code = CodeBlock.builder()
                .add("return $L.$L($S$L).$L(", PROVIDER_INTERFACE, SEND, rpcMethodName, paramsArgument, THEN_APPLY_ASYNC);

        if (returnsVoid(method, context)) {
            code.add("$L -> null", CALL_BACK_ARG);
        } else {
            code
                    .add("$L -> ", CALL_BACK_ARG)
                    .add(decoder.apply(getFutureParameter(method), CALL_BACK_ARG));
        }
        code.add(")");

        methodSpecBuilder.addStatement(code.build());
    }

    @Override
    protected boolean shouldBePassedToProvider(ExecutableElement method, VariableElement param, ProcessorContext context) {
        return true;
    }

    @Override
    protected void onParametersVisited(ExecutableElement method) {
        // This should be empty
    }

    @Override
    protected void ensureMethodHasAppropriateReturnType(ExecutableElement method,
                                                        TypeMirror returnType,
                                                        ProcessorContext context) throws ProcessingException {
        val expectedReturnType = context.erasure(
                context.getType(CompletableFuture.class));
        if (!context.isSameType(expectedReturnType, context.erasure(returnType))) {
            throw new ProcessingException(
                    interfaceElement,
                    "Method `%s.%s` has unexpected return type. Must be `%s`.",
                    interfaceElement.getQualifiedName().toString(),
                    method.getSimpleName(),
                    CompletableFuture.class.getSimpleName());
        }
    }

    private boolean returnsVoid(ExecutableElement method, ProcessorContext context) {
        val futureParameter = getFutureParameter(method);

        return context.isSameType(futureParameter, context.getType(Void.class));
    }

    private TypeMirror getFutureParameter(ExecutableElement method) {
        return ((DeclaredType) method.getReturnType()).getTypeArguments().get(0);
    }
}
