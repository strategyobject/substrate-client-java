package com.strategyobject.substrateclient.rpc.codegen.sections;

import com.google.common.base.Strings;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.strategyobject.substrateclient.common.codegen.ProcessingException;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import com.strategyobject.substrateclient.rpc.annotation.RpcSubscription;
import com.strategyobject.substrateclient.transport.RpcObject;
import lombok.NonNull;
import lombok.val;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static com.strategyobject.substrateclient.rpc.codegen.sections.Constants.*;

class RpcSubscriptionProcessor extends RpcMethodProcessor<RpcSubscription> {
    private static final String CALL_BACK_ARG = "r";
    private static final String CALL_BACK_EX_ARG = "e";
    private static final String CALL_BACK_PROXY = "callbackProxy";
    private static final String ID = "id";

    private boolean hasCallback = false;
    private String callbackName;
    private TypeMirror callbackParameter = null;
    private TypeMirror callbackType;
    private TypeMirror exceptionType;

    public RpcSubscriptionProcessor(@NonNull TypeElement interfaceElement) {
        super(RpcSubscription.class, interfaceElement);
    }

    @Override
    protected void onProcessStarting(ProcessorContext context) {
        callbackType = context.getType(BiConsumer.class);
        exceptionType = context.getType(Exception.class);
    }

    @Override
    protected void callProviderInterface(MethodSpec.Builder methodSpecBuilder,
                                         ExecutableElement method,
                                         String section,
                                         RpcSubscription annotation,
                                         ProcessorContext context,
                                         BiFunction<TypeMirror, String, CodeBlock> decoder) {
        val typeName = String.format(RPC_METHOD_NAME_TEMPLATE, section, annotation.type());
        val subscribeMethod = String.format(RPC_METHOD_NAME_TEMPLATE, section, annotation.subscribeMethod());
        val unsubscribeMethod = String.format(RPC_METHOD_NAME_TEMPLATE, section, annotation.unsubscribeMethod());

        val callBackCode = CodeBlock.builder()
                .add("$1T<$2T, $3T> $4L = ($5L, $6L) -> { $7N.$8L($5L, ",
                        BiConsumer.class,
                        Exception.class,
                        RpcObject.class,
                        CALL_BACK_PROXY,
                        CALL_BACK_EX_ARG,
                        CALL_BACK_ARG,
                        callbackName,
                        ACCEPT);

        if (isCallbackResultVoid(context)) {
            callBackCode.add("null");
        } else {
            callBackCode.add(decoder.apply(callbackParameter, CALL_BACK_ARG));
        }

        callBackCode.add("); }");

        methodSpecBuilder.addStatement(callBackCode.build())
                .addStatement(CodeBlock.builder()
                        .add("return $L.$L($S, $S, $L, $L)",
                                PROVIDER_INTERFACE,
                                SUBSCRIBE,
                                typeName,
                                subscribeMethod,
                                PARAMS_VAR,
                                CALL_BACK_PROXY)
                        .add(".$1L($2L -> () -> $3L.$4L($5S, $6S, $2L))",
                                THEN_APPLY_ASYNC,
                                ID,
                                PROVIDER_INTERFACE,
                                UNSUBSCRIBE,
                                typeName,
                                unsubscribeMethod)
                        .build());
    }

    @Override
    protected boolean shouldBePassedToProvider(ExecutableElement method, VariableElement param, ProcessorContext context) throws ProcessingException {
        val paramType = param.asType();
        if (!context.isSameType(context.erasure(paramType), context.erasure(callbackType))) { // is not callback
            return true;
        }

        if (hasCallback) {
            throw new ProcessingException(
                    interfaceElement,
                    "Method `%s.%s` contains more than one callback.",
                    interfaceElement.getQualifiedName().toString(),
                    method.getSimpleName());
        }

        callbackParameter = ((DeclaredType) paramType).getTypeArguments().get(1);
        if (!context.isSameType(exceptionType, ((DeclaredType) paramType).getTypeArguments().get(0))) {
            throw new ProcessingException(
                    interfaceElement,
                    "Method `%s.%s` contains the incorrect callback. Must be %s<%s, T>.",
                    interfaceElement.getQualifiedName().toString(),
                    method.getSimpleName(),
                    BiConsumer.class.getSimpleName(),
                    Exception.class.getSimpleName());
        }

        hasCallback = true;
        callbackName = param.getSimpleName().toString();

        return false;
    }

    @Override
    protected void onParametersVisited(ExecutableElement method) throws ProcessingException {
        if (!hasCallback) {
            throw new ProcessingException(
                    interfaceElement,
                    "Method `%s.%s` doesn't contain a callback.",
                    interfaceElement.getQualifiedName().toString(),
                    method.getSimpleName());
        }
    }

    @Override
    protected void ensureMethodHasAppropriateReturnType(ExecutableElement method, TypeMirror returnType, ProcessorContext context) throws ProcessingException {
        val futureType = context.erasure(context.getType(CompletableFuture.class));
        if (context.isSameType(futureType, context.erasure(returnType))) { // CompletableFuture<>
            val supplier = context.erasure(context.getType(Supplier.class));
            val futureParameter = ((DeclaredType) returnType).getTypeArguments().get(0);

            if (context.isSameType(supplier, context.erasure(futureParameter))) { // CompletableFuture<Supplier<>>
                val supplierParameter = ((DeclaredType) futureParameter).getTypeArguments().get(0);

                if (context.isSameType(futureType, context.erasure(supplierParameter))) { // CompletableFuture<Supplier<CompletableFuture<>>>
                    val boolType = context.getType(Boolean.class);
                    val subFutureParameter = ((DeclaredType) supplierParameter).getTypeArguments().get(0);

                    if (context.isSameType(boolType, subFutureParameter)) { // CompletableFuture<Supplier<CompletableFuture<Boolean>>>
                        return;
                    }
                }
            }
        }

        throw new ProcessingException(
                interfaceElement,
                "Method `%s.%s` has unexpected return type. Must be `%3$s<%4$s<%3$s<%5$s>>>`.",
                interfaceElement.getQualifiedName().toString(),
                method.getSimpleName(),
                CompletableFuture.class.getSimpleName(),
                Supplier.class.getSimpleName(),
                Boolean.class.getSimpleName());
    }

    @Override
    protected void ensureAnnotationIsFilled(ExecutableElement method, RpcSubscription annotation) throws ProcessingException {
        if (Strings.isNullOrEmpty(annotation.type())) {
            throw new ProcessingException(
                    interfaceElement,
                    "`@%s` of `%s.%s` contains null or empty `type`.",
                    annotation.getClass().getSimpleName(),
                    interfaceElement.getQualifiedName().toString(),
                    method.getSimpleName());
        }

        if (Strings.isNullOrEmpty(annotation.subscribeMethod())) {
            throw new ProcessingException(
                    interfaceElement,
                    "`@%s` of `%s.%s` contains null or empty `subscribeMethod`.",
                    annotation.getClass().getSimpleName(),
                    interfaceElement.getQualifiedName().toString(),
                    method.getSimpleName());
        }

        if (Strings.isNullOrEmpty(annotation.unsubscribeMethod())) {
            throw new ProcessingException(
                    interfaceElement,
                    "`@%s` of `%s.%s` contains null or empty `unsubscribeMethod`.",
                    annotation.getClass().getSimpleName(),
                    interfaceElement.getQualifiedName().toString(),
                    method.getSimpleName());
        }
    }

    private boolean isCallbackResultVoid(ProcessorContext context) {
        return context.isSameType(callbackParameter,
                context.getType(Void.class));
    }
}
