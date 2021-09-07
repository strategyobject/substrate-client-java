package com.strategyobject.substrateclient.rpc.codegen;

import com.squareup.javapoet.TypeSpec;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@RequiredArgsConstructor
abstract class RpcInterfaceMethodProcessor {
    @NonNull
    protected final TypeElement interfaceElement;

    abstract void process(@NonNull String section,
                          @NonNull ExecutableElement method,
                          @NonNull Types typeUtils,
                          @NonNull TypeSpec.Builder typeSpecBuilder,
                          @NonNull Elements elementUtils) throws ProcessingException;
}
