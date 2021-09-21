package com.strategyobject.substrateclient.common.codegen;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.WildcardTypeName;
import lombok.NonNull;
import lombok.val;

import javax.lang.model.element.TypeElement;
import java.util.Collections;

public final class JavaPoet {
    public static ParameterizedTypeName parameterizeClass(@NonNull TypeElement classElement, TypeName... parameters) {
        return ParameterizedTypeName.get(ClassName.get(classElement), parameters);
    }

    public static TypeName setEachGenericParameterAs(@NonNull TypeElement classElement, TypeName parameter) {
        val classTypeParametersSize = classElement.getTypeParameters().size();
        return classTypeParametersSize > 0 ?
                parameterizeClass(
                        classElement,
                        Collections.nCopies(classTypeParametersSize, parameter)
                                .toArray(new TypeName[classTypeParametersSize])) :
                TypeName.get(classElement.asType());
    }

    public static TypeName setEachGenericParameterAsWildcard(@NonNull TypeElement classElement) {
        return setEachGenericParameterAs(classElement, WildcardTypeName.subtypeOf(Object.class));
    }

    private JavaPoet() {
    }
}
