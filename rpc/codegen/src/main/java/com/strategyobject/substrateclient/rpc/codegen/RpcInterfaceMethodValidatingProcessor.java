package com.strategyobject.substrateclient.rpc.codegen;

import com.squareup.javapoet.TypeSpec;
import com.strategyobject.substrateclient.rpc.codegen.annotations.RpcCall;
import com.strategyobject.substrateclient.rpc.codegen.annotations.RpcSubscription;
import lombok.NonNull;
import lombok.val;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

class RpcInterfaceMethodValidatingProcessor extends RpcInterfaceMethodProcessor {
    public RpcInterfaceMethodValidatingProcessor(@NonNull TypeElement interfaceElement) {
        super(interfaceElement);
    }

    @Override
    void process(@NonNull String section,
                 @NonNull ExecutableElement method,
                 @NonNull Types typeUtils,
                 TypeSpec.@NonNull Builder typeSpecBuilder,
                 @NonNull Elements elementUtils) throws ProcessingException {

        ensureMethodIsNotAbstractOrProperlyAnnotated(method);
    }

    private void ensureMethodIsNotAbstractOrProperlyAnnotated(Element method) throws ProcessingException {
        val modifiers = method.getModifiers();
        val requiresMethod = method.getAnnotation(RpcCall.class) != null;
        val requiresSubscription = method.getAnnotation(RpcSubscription.class) != null;

        // Ensure method is not abstract or annotated with `RpcMethod` or `RpcSubscription`
        if (!(modifiers.contains(Modifier.STATIC) || modifiers.contains(Modifier.DEFAULT))
                && !(requiresMethod || requiresSubscription)) {
            throw new ProcessingException(
                    interfaceElement,
                    "Method `%s` can't be constructed because it doesn't have `@%s` or `@%s` annotation.",
                    method.getSimpleName().toString(),
                    RpcCall.class.getSimpleName(),
                    RpcSubscription.class.getSimpleName());
        }

        // Ensure method doesn't have ambiguous annotations
        if (requiresMethod && requiresSubscription) {
            throw new ProcessingException(
                    interfaceElement,
                    "Method `%s` can't be constructed because it has ambiguous annotations. Only one of `@%s` and `@%s` should be chosen.",
                    method.getSimpleName().toString(),
                    RpcCall.class.getSimpleName(),
                    RpcSubscription.class.getSimpleName());
        }
    }
}
