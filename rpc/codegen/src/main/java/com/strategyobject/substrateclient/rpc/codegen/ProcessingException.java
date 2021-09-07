package com.strategyobject.substrateclient.rpc.codegen;

import javax.lang.model.element.TypeElement;

public class ProcessingException extends Exception {
    private final TypeElement element;

    public ProcessingException(TypeElement element, String message, Object... args) {
        super(String.format(message, args));
        this.element = element;
    }
}
