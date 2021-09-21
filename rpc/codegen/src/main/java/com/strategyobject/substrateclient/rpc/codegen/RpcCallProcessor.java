package com.strategyobject.substrateclient.rpc.codegen;

import com.google.common.base.Strings;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcCall;
import lombok.NonNull;
import lombok.val;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.concurrent.CompletableFuture;

import static com.strategyobject.substrateclient.rpc.codegen.Constants.RPC_METHOD_NAME_TEMPLATE;

class RpcCallProcessor extends RpcInterfaceMethodProcessor {
    public RpcCallProcessor(@NonNull TypeElement interfaceElement) {
        super(interfaceElement);
    }

    @Override
    void process(@NonNull String section,
                 @NonNull ExecutableElement method,
                 @NonNull Types typeUtils,
                 TypeSpec.@NonNull Builder typeSpecBuilder,
                 @NonNull Elements elementUtils) throws ProcessingException {
        val callAnnotation = method.getAnnotation(RpcCall.class);
        if (callAnnotation == null) {
            return;
        }

        ensureAnnotationIsFilled(method, callAnnotation);

        typeSpecBuilder.addMethod(createMethod(section, method, callAnnotation, typeUtils, elementUtils));
    }

    private void ensureAnnotationIsFilled(ExecutableElement method, RpcCall methodAnnotation)
            throws ProcessingException {
        if (Strings.isNullOrEmpty(methodAnnotation.method())) {
            throw new ProcessingException(
                    interfaceElement,
                    "`@%s` of `%s.%s` contains null or empty `method`.",
                    methodAnnotation.getClass().getSimpleName(),
                    interfaceElement.getQualifiedName().toString(),
                    method.getSimpleName());
        }
    }

    private MethodSpec createMethod(String section,
                                    ExecutableElement method,
                                    RpcCall methodAnnotation,
                                    Types typeUtils,
                                    Elements elementUtils) throws ProcessingException {
        val returnType = method.getReturnType();
        ensureMethodHasAppropriateReturnType(method, typeUtils, elementUtils, returnType);

        val futureParameter = ((DeclaredType) returnType).getTypeArguments().get(0);
        val isReturnVoid = typeUtils.isSameType(futureParameter,
                elementUtils.getTypeElement(Void.class.getTypeName()).asType());

        val methodSpecBuilder = MethodSpec.methodBuilder(method.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(TypeName.get(returnType));

        String paramsArgument;
        if (!method.getParameters().isEmpty()) {
            methodSpecBuilder.addStatement("java.util.List<Object> params = new java.util.ArrayList<Object>()");

            for (val param : method.getParameters()) {
                val parameterName = param.getSimpleName().toString();
                methodSpecBuilder
                        .addParameter(TypeName.get(param.asType()), parameterName)
                        .addStatement("params.add(parameterConverter.convert($N))", parameterName);
            }

            paramsArgument = ", params";
        } else {
            paramsArgument = "";
        }

        val methodName = String.format(RPC_METHOD_NAME_TEMPLATE, section, methodAnnotation.method());
        val thenApply = isReturnVoid
                ? "r -> null"
                : "resultConverter::convert";
        methodSpecBuilder.addStatement(
                "return providerInterface.send($S$L).thenApply($L)",
                methodName,
                paramsArgument,
                thenApply);

        return methodSpecBuilder.build();
    }

    private void ensureMethodHasAppropriateReturnType(ExecutableElement method,
                                                      Types typeUtils,
                                                      Elements elementUtils,
                                                      TypeMirror returnType) throws ProcessingException {
        val expectedReturnType = typeUtils.erasure(
                elementUtils.getTypeElement(CompletableFuture.class.getTypeName()).asType());
        if (!typeUtils.isSameType(expectedReturnType, typeUtils.erasure(returnType))) {
            throw new ProcessingException(
                    interfaceElement,
                    "Method `%s.%s` has unexpected return type. Must be `%s`.",
                    interfaceElement.getQualifiedName().toString(),
                    method.getSimpleName(),
                    CompletableFuture.class.getSimpleName());
        }
    }
}
