package com.strategyobject.substrateclient.common.codegen;

import javax.lang.model.type.TypeMirror;

public class TypeNotSupportedException extends IllegalArgumentException {
    public TypeNotSupportedException(TypeMirror type) {
        super("Type is not supported: " + type);
    }
}
