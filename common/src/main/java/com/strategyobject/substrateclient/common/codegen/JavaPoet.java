package com.strategyobject.substrateclient.common.codegen;

import com.squareup.javapoet.*;
import lombok.NonNull;
import lombok.val;

import javax.lang.model.element.TypeElement;
import java.util.Collections;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

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

    public static Map<String, Object> args(@NonNull Map<String, Object> map, @NonNull Object... params) {
        val target = IntStream.range(0, params.length)
                .boxed()
                .collect(toMap(i -> String.format("p_%s", i + 1), i -> params[i]));
        target.putAll(map);

        return target;
    }

    public static CodeBlock named(@NonNull String format, @NonNull Map<String, Object> aliases) {
        return CodeBlock.builder()
                .addNamed(format, aliases)
                .build();
    }

    private JavaPoet() {
    }
}
