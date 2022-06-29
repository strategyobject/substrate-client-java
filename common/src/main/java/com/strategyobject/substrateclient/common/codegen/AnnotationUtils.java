package com.strategyobject.substrateclient.common.codegen;

import com.google.common.base.Preconditions;
import com.squareup.javapoet.AnnotationSpec;
import lombok.NonNull;
import lombok.val;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.AnnotationMirror;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class AnnotationUtils {
    public static AnnotationMirror getAnnotationMirror(@NonNull AnnotatedConstruct target,
                                                       @NonNull Class<? extends Annotation> annotationType) {
        String annotationTypeName = annotationType.getCanonicalName();
        for (val annotationMirror : target.getAnnotationMirrors()) {
            if (annotationMirror.getAnnotationType().toString().equals(annotationTypeName)) {
                return annotationMirror;
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValueFromAnnotation(@NonNull AnnotationMirror annotation,
                                               @NonNull String elementName) {
        for (val entry : annotation.getElementValues().entrySet()) {
            if (entry.getKey().getSimpleName().toString().equals(elementName)) {
                return (T) entry.getValue().getValue();
            }
        }

        return null;
    }

    public static <T> T getValueFromAnnotation(@NonNull AnnotatedConstruct target,
                                               @NonNull Class<? extends Annotation> annotationType,
                                               @NonNull String elementName) {
        val annotationMirror = getAnnotationMirror(target, annotationType);
        return annotationMirror != null ?
                getValueFromAnnotation(annotationMirror, elementName) :
                null;
    }

    public static AnnotationSpec suppressWarnings(String... warnings) {
        Preconditions.checkArgument(warnings != null && warnings.length > 0);
        val format = IntStream.range(0, warnings.length)
                .boxed()
                .map(i -> "$S")
                .collect(Collectors.joining(",", "{", "}"));

        return AnnotationSpec.builder(SuppressWarnings.class)
                .addMember("value", format, (Object[]) warnings)
                .build();
    }

    public static boolean isAnnotatedWith(AnnotatedConstruct annotated, Class<? extends Annotation> annotation) {
        return annotated.getAnnotation(annotation) != null;
    }

    @SafeVarargs
    public static boolean isAnnotatedWithAny(AnnotatedConstruct annotated, Class<? extends Annotation>... annotations) {
        return Arrays.stream(annotations).anyMatch(x -> annotated.getAnnotation(x) != null);
    }

    @SafeVarargs
    public static boolean isAnnotatedWithAll(AnnotatedConstruct annotated, Class<? extends Annotation>... annotations) {
        return Arrays.stream(annotations).allMatch(x -> annotated.getAnnotation(x) != null);
    }

    private AnnotationUtils() {
    }
}
