package com.strategyobject.substrateclient.rpc.codegen;

import com.google.common.base.Strings;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcSubscription;
import lombok.NonNull;
import lombok.val;
import lombok.var;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static com.strategyobject.substrateclient.rpc.codegen.Constants.RPC_METHOD_NAME_TEMPLATE;

class RpcSubscriptionProcessor extends RpcInterfaceMethodProcessor {
    public RpcSubscriptionProcessor(@NonNull TypeElement interfaceElement) {
        super(interfaceElement);
    }

    @Override
    void process(@NonNull String section,
                 @NonNull ExecutableElement method,
                 @NonNull Types typeUtils,
                 @NonNull TypeSpec.Builder typeSpecBuilder,
                 @NonNull Elements elementUtils) throws ProcessingException {
        val subscriptionAnnotation = method.getAnnotation(RpcSubscription.class);
        if (subscriptionAnnotation == null) {
            return;
        }

        ensureAnnotationIsFilled(method, subscriptionAnnotation);
        typeSpecBuilder.addMethod(createMethod(section, method, subscriptionAnnotation, typeUtils, elementUtils));
    }

    private MethodSpec createMethod(
            String section,
            ExecutableElement method,
            RpcSubscription subscriptionAnnotation,
            Types typeUtils,
            Elements elementUtils) throws ProcessingException {
        val returnType = method.getReturnType();
        ensureMethodHasAppropriateReturnType(method, typeUtils, elementUtils, returnType);

        val methodSpecBuilder = MethodSpec.methodBuilder(method.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(TypeName.get(returnType))
                .addStatement("java.util.List<Object> params = new java.util.ArrayList<Object>()");

        var hasCallback = false;
        val callbackType = elementUtils.getTypeElement(BiConsumer.class.getTypeName()).asType();
        val exceptionType = elementUtils.getTypeElement(Exception.class.getTypeName()).asType();
        TypeMirror callbackParameter = null;
        String callbackName = null;
        for (val param : method.getParameters()) {
            val parameterName = param.getSimpleName().toString();
            val paramType = param.asType();
            methodSpecBuilder.addParameter(TypeName.get(paramType), parameterName);

            if (typeUtils.isSameType(typeUtils.erasure(paramType), typeUtils.erasure(callbackType))) { // is callback
                if (hasCallback) {
                    throw new ProcessingException(
                            interfaceElement,
                            "Method `%s.%s` contains more than one callback.",
                            interfaceElement.getQualifiedName().toString(),
                            method.getSimpleName());
                }

                callbackParameter = ((DeclaredType) paramType).getTypeArguments().get(1);
                if (!typeUtils.isSameType(exceptionType, ((DeclaredType) paramType).getTypeArguments().get(0))) {
                    throw new ProcessingException(
                            interfaceElement,
                            "Method `%s.%s` contains the incorrect callback. Must be %s<%s, T>.",
                            interfaceElement.getQualifiedName().toString(),
                            method.getSimpleName(),
                            BiConsumer.class.getSimpleName(),
                            Exception.class.getSimpleName());
                }

                hasCallback = true;
                callbackName = parameterName;
            } else {
                methodSpecBuilder.addStatement("params.add(parameterConverter.convert($N))", parameterName);
            }
        }

        if (!hasCallback) {
            throw new ProcessingException(
                    interfaceElement,
                    "Method `%s.%s` doesn't contain a callback.",
                    interfaceElement.getQualifiedName().toString(),
                    method.getSimpleName());
        }

        val convertCall = typeUtils.isSameType(callbackParameter,
                elementUtils.getTypeElement(Void.class.getTypeName()).asType()) // if callback is BiConsumer<Exception, Void>
                ? "null"
                : "resultConverter.convert(r)";

        val typeName = String.format(RPC_METHOD_NAME_TEMPLATE, section, subscriptionAnnotation.type());
        val subscribeMethod = String.format(RPC_METHOD_NAME_TEMPLATE, section, subscriptionAnnotation.subscribeMethod());
        val unsubscribeMethod = String.format(RPC_METHOD_NAME_TEMPLATE, section, subscriptionAnnotation.unsubscribeMethod());
        methodSpecBuilder
                .addStatement(
                        "$T<$T, ?> callbackProxy = (e, r) -> { $N.accept(e, $L); }",
                        BiConsumer.class,
                        Exception.class,
                        callbackName,
                        convertCall)
                .addStatement(
                        "return providerInterface.subscribe($1S, $2S, params, callbackProxy)" +
                                ".thenApply(id -> () -> providerInterface.unsubscribe($1S, $3S, id))",
                        typeName,
                        subscribeMethod,
                        unsubscribeMethod);

        return methodSpecBuilder.build();
    }

    private void ensureAnnotationIsFilled(ExecutableElement method, RpcSubscription subscriptionAnnotation)
            throws ProcessingException {
        if (Strings.isNullOrEmpty(subscriptionAnnotation.type())) {
            throw new ProcessingException(
                    interfaceElement,
                    "`@%s` of `%s.%s` contains null or empty `type`.",
                    subscriptionAnnotation.getClass().getSimpleName(),
                    interfaceElement.getQualifiedName().toString(),
                    method.getSimpleName());
        }

        if (Strings.isNullOrEmpty(subscriptionAnnotation.subscribeMethod())) {
            throw new ProcessingException(
                    interfaceElement,
                    "`@%s` of `%s.%s` contains null or empty `subscribeMethod`.",
                    subscriptionAnnotation.getClass().getSimpleName(),
                    interfaceElement.getQualifiedName().toString(),
                    method.getSimpleName());
        }

        if (Strings.isNullOrEmpty(subscriptionAnnotation.unsubscribeMethod())) {
            throw new ProcessingException(
                    interfaceElement,
                    "`@%s` of `%s.%s` contains null or empty `unsubscribeMethod`.",
                    subscriptionAnnotation.getClass().getSimpleName(),
                    interfaceElement.getQualifiedName().toString(),
                    method.getSimpleName());
        }
    }

    private void ensureMethodHasAppropriateReturnType(ExecutableElement method,
                                                      Types typeUtils,
                                                      Elements elementUtils,
                                                      TypeMirror returnType) throws ProcessingException {
        val futureType = typeUtils.erasure(elementUtils.getTypeElement(CompletableFuture.class.getTypeName()).asType());
        if (typeUtils.isSameType(futureType, typeUtils.erasure(returnType))) { // CompletableFuture<>
            val supplier = typeUtils.erasure(elementUtils.getTypeElement(Supplier.class.getTypeName()).asType());
            val futureParameter = ((DeclaredType) returnType).getTypeArguments().get(0);

            if (typeUtils.isSameType(supplier, typeUtils.erasure(futureParameter))) { // CompletableFuture<Supplier<>>
                val supplierParameter = ((DeclaredType) futureParameter).getTypeArguments().get(0);

                if (typeUtils.isSameType(futureType, typeUtils.erasure(supplierParameter))) { // CompletableFuture<Supplier<CompletableFuture<>>>
                    val boolType = elementUtils.getTypeElement(Boolean.class.getTypeName()).asType();
                    val subFutureParameter = ((DeclaredType) supplierParameter).getTypeArguments().get(0);

                    if (typeUtils.isSameType(boolType, subFutureParameter)) { // CompletableFuture<Supplier<CompletableFuture<Boolean>>>
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
}
