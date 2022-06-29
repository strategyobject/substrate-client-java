package com.strategyobject.substrateclient.common.codegen;

import javax.lang.model.type.TypeMirror;

public class TypeNotSupportedException extends IllegalArgumentException {
    public TypeNotSupportedException(String type) {
        super("Type is not supported: " + type);
    }

    public TypeNotSupportedException(TypeMirror type) {
        this(type.toString());
    }
}
