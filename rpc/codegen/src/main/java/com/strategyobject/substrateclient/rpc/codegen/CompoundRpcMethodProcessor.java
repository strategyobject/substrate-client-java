package com.strategyobject.substrateclient.rpc.codegen;

import com.squareup.javapoet.TypeSpec;
import lombok.NonNull;
import lombok.var;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;

class CompoundRpcMethodProcessor extends RpcInterfaceMethodProcessor {
    private final List<RpcInterfaceMethodProcessor> processors;

    public CompoundRpcMethodProcessor(@NonNull TypeElement interfaceElement,
                                      @NonNull List<RpcInterfaceMethodProcessor> processors) {
        super(interfaceElement);
        this.processors = processors;
    }

    @Override
    void process(@NonNull String section,
                 @NonNull ExecutableElement method,
                 @NonNull Types typeUtils,
                 @NonNull TypeSpec.Builder typeSpecBuilder,
                 @NonNull Elements elementUtils) throws ProcessingException {
        for (var processor : processors) {
            processor.process(section, method, typeUtils, typeSpecBuilder, elementUtils);
        }
    }
}
