package com.strategyobject.substrateclient.rpc.codegen.sections;

import com.squareup.javapoet.TypeSpec;
import com.strategyobject.substrateclient.common.codegen.ProcessingException;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import lombok.NonNull;
import lombok.var;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
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
                 @NonNull TypeSpec.Builder typeSpecBuilder,
                 @NonNull ProcessorContext context) throws ProcessingException {
        for (var processor : processors) {
            processor.process(section, method, typeSpecBuilder, context);
        }
    }
}
