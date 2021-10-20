package com.strategyobject.substrateclient.rpc.codegen.sections;

import com.squareup.javapoet.TypeSpec;
import com.strategyobject.substrateclient.common.codegen.ProcessingException;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

@RequiredArgsConstructor
abstract class RpcInterfaceMethodProcessor {
    @NonNull
    protected final TypeElement interfaceElement;

    abstract void process(@NonNull String section,
                          @NonNull ExecutableElement method,
                          @NonNull TypeSpec.Builder typeSpecBuilder,
                          @NonNull ProcessorContext context) throws ProcessingException;
}
