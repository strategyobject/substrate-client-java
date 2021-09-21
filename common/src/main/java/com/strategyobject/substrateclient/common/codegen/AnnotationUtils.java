package com.strategyobject.substrateclient.common.codegen;

import lombok.NonNull;
import lombok.val;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.AnnotationMirror;
import java.lang.annotation.Annotation;

public final class AnnotationUtils {
    public static AnnotationMirror getAnnotationMirror(@NonNull AnnotatedConstruct target,
                                                       @NonNull Class<? extends Annotation> annotationType) {
        String annotationTypeName = annotationType.getName();
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

    private AnnotationUtils() {
    }
}
