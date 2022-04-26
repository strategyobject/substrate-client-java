package com.strategyobject.substrateclient.pallet;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.strategyobject.substrateclient.common.codegen.ProcessingException;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import lombok.NonNull;
import lombok.var;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.List;

class CompoundMethodProcessor extends PalletMethodProcessor {
    private final List<PalletMethodProcessor> processors;

    public CompoundMethodProcessor(TypeElement typeElement, List<PalletMethodProcessor> processors) {
        super(typeElement);
        this.processors = processors;
    }

    @Override
    void process(@NonNull String palletName, @NonNull ExecutableElement method, TypeSpec.@NonNull Builder typeSpecBuilder, MethodSpec.Builder constructorBuilder, @NonNull ProcessorContext context) throws ProcessingException {
        for (var processor : processors) {
            processor.process(palletName,
                    method,
                    typeSpecBuilder,
                    constructorBuilder,
                    context);
        }
    }
}
