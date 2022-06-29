package com.strategyobject.substrateclient.scale.codegen;

import com.google.common.base.Strings;
import com.strategyobject.substrateclient.common.codegen.AnnotationUtils;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import com.strategyobject.substrateclient.common.codegen.TypeTraverser;
import com.strategyobject.substrateclient.common.codegen.TypeUtils;
import com.strategyobject.substrateclient.common.strings.StringUtils;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleGeneric;
import lombok.NonNull;
import lombok.val;
import lombok.var;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.TypeMirror;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.strategyobject.substrateclient.scale.codegen.ScaleProcessorHelper.SCALE_ANNOTATIONS_DEFAULT;

public class ScaleAnnotationParser {
    private static final String VALUE = "value";
    private static final String TEMPLATE = "template";
    private final ProcessorContext context;

    public ScaleAnnotationParser(@NonNull ProcessorContext context) {
        this.context = context;
    }

    public TypeTraverser.TypeTreeNode parse(@NonNull AnnotatedConstruct annotated) {
        val scaleType = AnnotationUtils.<TypeMirror>getValueFromAnnotation(annotated, Scale.class, VALUE);
        if (scaleType != null) {
            return new TypeTraverser.TypeTreeNode(scaleType);
        }

        val scaleGeneric = AnnotationUtils.getAnnotationMirror(annotated, ScaleGeneric.class);
        if (scaleGeneric != null) {
            val template = AnnotationUtils.<String>getValueFromAnnotation(scaleGeneric, TEMPLATE);
            val typesMap = getTypesMap(scaleGeneric);
            return parseTemplate(template, typesMap);
        }

        return null;
    }

    public TypeTraverser.TypeTreeNode parse(AnnotationMirror annotation) {
        if (context.isSameType(annotation.getAnnotationType(), context.getType(Scale.class))) {
            val scaleType = AnnotationUtils.<TypeMirror>getValueFromAnnotation(annotation, VALUE);

            return new TypeTraverser.TypeTreeNode(scaleType);
        }

        if (context.isSameType(annotation.getAnnotationType(), context.getType(ScaleGeneric.class))) {
            val template = AnnotationUtils.<String>getValueFromAnnotation(annotation, TEMPLATE);
            val typesMap = getTypesMap(annotation);

            return parseTemplate(template, typesMap);
        }

        return null;
    }

    private TypeTraverser.TypeTreeNode parseTemplate(String template, Map<String, TypeMirror> typesMap) {
        val indexes = StringUtils.allIndexesOfAny(template, "<,>");
        if (indexes.size() == 0) {
            return new TypeTraverser.TypeTreeNode(getMappedType(typesMap, template.trim()));
        }
        if (indexes.get(0) == 0) {
            throw new IllegalArgumentException("Template cannot begin with a special character");
        }

        val firstIndex = indexes.get(0);
        val rootType = getMappedType(typesMap, template.substring(0, firstIndex).trim());
        val root = new TypeTraverser.TypeTreeNode(rootType);
        var node = root;
        var nameStart = firstIndex + 1;
        try {
            for (int i = 0; i < indexes.size(); i++) {
                val index = indexes.get(i);
                val op = template.charAt(index);
                if (op == '>') {
                    node = node.getParent();
                    nameStart = index + 1;
                    continue;
                }

                val nameEnd = i < indexes.size() - 1 ? indexes.get(i + 1) : template.length();
                val type = getMappedType(typesMap, template.substring(nameStart, nameEnd).trim());
                val newNode = new TypeTraverser.TypeTreeNode(type);

                if (template.charAt(index) == '<') {
                    node.add(newNode);
                } else if (template.charAt(index) == ',') {
                    node.getParent().add(newNode);
                }

                node = newNode;
                nameStart = nameEnd + 1;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Wrong template", e);
        }

        if (root != node) {
            throw new IllegalArgumentException("Template brackets don't match");
        }

        return root;
    }

    private TypeMirror getMappedType(Map<String, TypeMirror> typesMap, String name) {
        val type = typesMap.get(name);
        return type == null || context.isAssignable(type, context.getType(SCALE_ANNOTATIONS_DEFAULT)) ? null : type;
    }

    private Map<String, TypeMirror> getTypesMap(AnnotationMirror scaleGeneric) {
        val annotations = AnnotationUtils.<List<AnnotationMirror>>getValueFromAnnotation(scaleGeneric, "types");
        val result = new HashMap<String, TypeMirror>(Objects.requireNonNull(annotations).size());
        for (val annotation : annotations) {
            var type = AnnotationUtils.<TypeMirror>getValueFromAnnotation(annotation, VALUE);
            var name = AnnotationUtils.<String>getValueFromAnnotation(annotation, "name");
            validateScaleAnnotationIsNotEmpty(name, type);

            if (type == null) {
                type = context.getType(SCALE_ANNOTATIONS_DEFAULT);
            }

            if (Strings.isNullOrEmpty(name)) {
                name = TypeUtils.getSimpleName(type);
            }

            result.put(name, type);
        }

        return result;
    }

    private void validateScaleAnnotationIsNotEmpty(String name, TypeMirror type) {
        if (Strings.isNullOrEmpty(name) && type == null) {
            throw new IllegalArgumentException("Empty Scale annotation.");
        }
    }
}
