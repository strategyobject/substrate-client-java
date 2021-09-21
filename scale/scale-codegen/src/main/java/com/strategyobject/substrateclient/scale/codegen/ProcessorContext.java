package com.strategyobject.substrateclient.scale.codegen;

import com.strategyobject.substrateclient.scale.ScaleSelfWritable;
import com.strategyobject.substrateclient.scale.annotations.Default;
import lombok.Getter;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@Getter
public class ProcessorContext {
    public static final String SCALE_SELF_WRITABLE = ScaleSelfWritable.class.getCanonicalName();
    public static final String SCALE_ANNOTATIONS_DEFAULT = Default.class.getCanonicalName();
    private static final String READER_NAME_TEMPLATE = "%sReader";
    private static final String WRITER_NAME_TEMPLATE = "%sWriter";

    private final Types typeUtils;
    private final Elements elementUtils;
    private final TypeMirror scaleSelfWritableRawType;
    private final TypeMirror scaleAnnotationsDefaultType;

    public ProcessorContext(Types typeUtils, Elements elementUtils) {
        this.typeUtils = typeUtils;
        this.elementUtils = elementUtils;
        this.scaleSelfWritableRawType = typeUtils.erasure(elementUtils.getTypeElement(SCALE_SELF_WRITABLE).asType());
        this.scaleAnnotationsDefaultType = elementUtils.getTypeElement(SCALE_ANNOTATIONS_DEFAULT).asType();
    }

    public String getPackageName(TypeElement classElement) {
        return elementUtils.getPackageOf(classElement).getQualifiedName().toString();
    }

    public String getReaderName(String className) {
        return String.format(READER_NAME_TEMPLATE, className);
    }

    public String getWriterName(String className) {
        return String.format(WRITER_NAME_TEMPLATE, className);
    }

    public boolean isSubtypeOf(TypeMirror candidate, TypeMirror supertype) {
        return typeUtils.isAssignable(candidate, supertype);
    }

    public boolean isSubtypeOfScaleAnnotationsDefault(TypeMirror type) {
        return isSubtypeOf(type, scaleAnnotationsDefaultType);
    }

    public boolean isSubtypeOfScaleSelfWritable(TypeMirror type) {
        return isSubtypeOf(type, scaleSelfWritableRawType);
    }

    public boolean isGeneric(TypeMirror type) {
        return ((TypeElement) typeUtils.asElement(type))
                .getTypeParameters()
                .size() > 0;
    }
}
