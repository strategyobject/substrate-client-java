package com.strategyobject.substrateclient.common.codegen;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@RequiredArgsConstructor
@Getter
public class ProcessorContext {
    private final @NonNull Types typeUtils;
    private final @NonNull Elements elementUtils;
    private final @NonNull Filer filer;
    private final @NonNull Messager messager;

    public String getPackageName(@NonNull TypeElement classElement) {
        return elementUtils.getPackageOf(classElement).getQualifiedName().toString();
    }

    public boolean isAssignable(@NonNull TypeMirror candidate, @NonNull TypeMirror supertype) {
        return typeUtils.isAssignable(candidate, supertype);
    }

    public boolean isSubtype(@NonNull TypeMirror candidate, @NonNull TypeMirror supertype) {
        return typeUtils.isSubtype(candidate, supertype);
    }

    public boolean isNonGeneric(@NonNull TypeMirror type) {
        if (type instanceof ArrayType) {
            return isNonGeneric(((ArrayType) type).getComponentType());
        }

        return type.getKind().isPrimitive() ||
                ((TypeElement) typeUtils.asElement(type))
                        .getTypeParameters()
                        .isEmpty();
    }

    public TypeMirror erasure(@NonNull TypeMirror type) {
        return typeUtils.erasure(type);
    }

    public TypeMirror getBoxed(@NonNull TypeMirror type) {
        return type instanceof PrimitiveType ?
                typeUtils.boxedClass((PrimitiveType) type).asType() :
                type;
    }

    public TypeMirror getType(Class<?> clazz) {
        return elementUtils.getTypeElement(clazz.getCanonicalName()).asType();
    }

    public boolean isSameType(@NonNull TypeMirror left, @NonNull TypeMirror right) {
        return typeUtils.isSameType(left, right);
    }

    public void error(Exception exception) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                exception.getMessage()
        );
    }

    public void error(Element e, Exception exception) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                exception.getMessage(),
                e
        );
    }

    public void error(Element e, String message, Object... args) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(message, args),
                e
        );
    }
}
