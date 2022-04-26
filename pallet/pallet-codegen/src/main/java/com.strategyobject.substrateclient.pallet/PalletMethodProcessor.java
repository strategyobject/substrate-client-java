package com.strategyobject.substrateclient.pallet;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.strategyobject.substrateclient.common.codegen.ProcessingException;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

@RequiredArgsConstructor
abstract class PalletMethodProcessor {
    @NonNull
    protected final TypeElement palletElement;

    abstract void process(@NonNull String palletName,
                          @NonNull ExecutableElement method,
                          @NonNull TypeSpec.Builder typeSpecBuilder,
                          MethodSpec.Builder constructorBuilder,
                          @NonNull ProcessorContext context) throws ProcessingException;
}
