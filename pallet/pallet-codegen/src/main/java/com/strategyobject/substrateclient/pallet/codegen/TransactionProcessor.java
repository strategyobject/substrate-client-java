package com.strategyobject.substrateclient.pallet.codegen;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.strategyobject.substrateclient.common.codegen.AnnotationUtils;
import com.strategyobject.substrateclient.common.codegen.ProcessingException;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import com.strategyobject.substrateclient.pallet.annotation.Storage;
import com.strategyobject.substrateclient.pallet.annotation.Transaction;
import lombok.NonNull;
import lombok.val;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

class TransactionProcessor extends PalletMethodProcessor {
    public TransactionProcessor(@NonNull TypeElement palletElement) {
        super(palletElement);
    }

    @Override
    void process(@NonNull String palletName, @NonNull ExecutableElement method, TypeSpec.@NonNull Builder typeSpecBuilder, MethodSpec.Builder constructorBuilder, @NonNull ProcessorContext context) throws ProcessingException {
        val annotation = AnnotationUtils.getAnnotationMirror(method, Transaction.class);
        if (annotation == null) {
            return;
        }
    }
}
