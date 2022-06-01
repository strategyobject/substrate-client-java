package com.strategyobject.substrateclient.common.codegen;

import com.google.common.collect.Streams;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;

import javax.lang.model.type.*;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class TypeTraverser<T> {
    private final Class<T> clazz;

    public TypeTraverser(Class<T> clazz) {
        this.clazz = clazz;
    }

    protected abstract T whenTypeVar(@NonNull TypeVariable type, TypeMirror override);

    protected abstract T whenPrimitiveType(@NonNull PrimitiveType type, TypeMirror override);

    protected abstract T whenNonGenericType(@NonNull DeclaredType type, TypeMirror override);

    protected abstract T whenGenericType(@NonNull DeclaredType type, TypeMirror override, @NonNull T[] subtypes);

    protected abstract T whenArrayPrimitiveType(@NonNull ArrayType type, TypeMirror override);

    protected abstract T whenArrayType(@NonNull ArrayType type, TypeMirror override, @NonNull T subtype);

    protected boolean doTraverseArguments(@NonNull DeclaredType type, TypeMirror override) {
        return true;
    }

    @SuppressWarnings("unchecked")
    public T traverse(@NonNull TypeMirror type) {
        if (type.getKind() == TypeKind.TYPEVAR) {
            return whenTypeVar((TypeVariable) type, null);
        }

        if (type.getKind().isPrimitive()) {
            return whenPrimitiveType((PrimitiveType) type, null);
        }

        if (type instanceof ArrayType) {
            val arrayType = (ArrayType) type;
            return arrayType.getComponentType().getKind().isPrimitive() ?
                    whenArrayPrimitiveType(arrayType, null) :
                    whenArrayType(
                            arrayType,
                            null,
                            traverse(arrayType.getComponentType()));
        }

        if (!(type instanceof DeclaredType)) {
            throw new TypeNotSupportedException(type);
        }

        val declaredType = (DeclaredType) type;
        val typeArguments = getTypeArgumentsOrDefault(declaredType, null);
        if (typeArguments.size() == 0) {
            return whenNonGenericType(declaredType, null);
        }

        return whenGenericType(
                declaredType,
                null,
                typeArguments.stream()
                        .map(this::traverse)
                        .toArray(x -> (T[]) Array.newInstance(clazz, typeArguments.size())));
    }

    @SuppressWarnings({"unchecked", "UnstableApiUsage"})
    public T traverse(@NonNull TypeMirror type, @NonNull TypeTraverser.TypeTreeNode typeOverride) {
        if (type.getKind() == TypeKind.TYPEVAR) {
            return whenTypeVar((TypeVariable) type, typeOverride.type);
        }

        if (type.getKind().isPrimitive()) {
            return whenPrimitiveType((PrimitiveType) type, typeOverride.type);
        }

        if (type instanceof ArrayType) {
            val arrayType = (ArrayType) type;
            if (arrayType.getComponentType().getKind().isPrimitive()) {
                return whenArrayPrimitiveType(arrayType, typeOverride.type);
            }

            switch (typeOverride.children.size()) {
                case 0:
                    return whenArrayType(
                            arrayType,
                            typeOverride.type,
                            traverse(arrayType.getComponentType()));
                case 1:
                    return whenArrayType(
                            arrayType,
                            typeOverride.type,
                            traverse(arrayType.getComponentType(), typeOverride.children.get(0)));
                default:
                    throw new IllegalArgumentException("Array type cannot be overridden by a generic type with more than one parameter");
            }
        }

        if (!(type instanceof DeclaredType)) {
            throw new TypeNotSupportedException(type);
        }

        val declaredType = (DeclaredType) type;
        val typeArguments = getTypeArgumentsOrDefault(declaredType, typeOverride.type);
        val typeArgumentsSize = typeArguments.size();
        val typeOverrideSize = typeOverride.children.size();
        if (typeIsNonGeneric(typeArgumentsSize, typeOverrideSize)) {
            return whenNonGenericType(declaredType, typeOverride.type);
        }

        if (typeIsOverriddenByNonGeneric(typeOverrideSize)) {
            return whenGenericType(
                    declaredType,
                    typeOverride.type,
                    typeArguments.stream()
                            .map(this::traverse)
                            .toArray(x -> (T[]) Array.newInstance(clazz, typeArguments.size())));
        }

        if (typeOverrideSize != typeArgumentsSize) {
            throw new IllegalArgumentException("Generic type cannot be overridden by a generic with different amount of parameters");
        }

        return whenGenericType(
                declaredType,
                typeOverride.type,
                Streams.zip(
                                typeArguments.stream(),
                                typeOverride.children.stream(),
                                this::traverse)
                        .toArray(x -> (T[]) Array.newInstance(clazz, typeArguments.size())));
    }

    @SuppressWarnings({"unchecked"})
    public T traverse(@NonNull TypeTraverser.TypeTreeNode typeOverride) {
        if (typeOverride.type.getKind().isPrimitive()) {
            return whenPrimitiveType((PrimitiveType) typeOverride.type, typeOverride.type);
        }

        if (typeOverride.type instanceof ArrayType) {
            val arrayType = (ArrayType) typeOverride.type;
            return arrayType.getComponentType().getKind().isPrimitive() ?
                    whenArrayPrimitiveType(arrayType, arrayType) :
                    whenArrayType(
                            arrayType,
                            arrayType,
                            traverse(arrayType.getComponentType()));
        }

        if (!(typeOverride.type instanceof DeclaredType)) {
            throw new TypeNotSupportedException(typeOverride.type);
        }

        val declaredType = (DeclaredType) typeOverride.type;
        if (typeOverride.children.isEmpty()) {
            return whenNonGenericType(declaredType, typeOverride.type);
        }

        return whenGenericType(
                declaredType,
                typeOverride.type,
                typeOverride.children.stream()
                        .map(this::traverse)
                        .toArray(x -> (T[]) Array.newInstance(clazz, typeOverride.children.size())));
    }

    private List<? extends TypeMirror> getTypeArgumentsOrDefault(DeclaredType declaredType, TypeMirror override) {
        return (doTraverseArguments(declaredType, override) ?
                declaredType.getTypeArguments() :
                Collections.emptyList());
    }

    private boolean typeIsNonGeneric(int typeArgumentsSize, int typeOverrideSize) {
        if (typeArgumentsSize == 0) {
            if (typeOverrideSize > 0) {
                throw new IllegalArgumentException("Non generic type cannot be overridden by generic");
            }

            return true;
        }

        return false;
    }

    private boolean typeIsOverriddenByNonGeneric(int typeOverrideSize) {
        return typeOverrideSize == 0;
    }


    public static class TypeTreeNode {
        @Getter
        private final TypeMirror type;
        private final List<TypeTreeNode> children;

        @Getter
        private TypeTreeNode parent;

        public TypeTreeNode(TypeMirror type) {
            this.type = type;
            this.children = new LinkedList<>();
        }

        public void add(TypeTreeNode node) {
            node.parent = this;
            children.add(node);
        }
    }
}
