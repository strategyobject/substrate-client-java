package com.strategyobject.substrateclient.common.codegen;

import com.google.common.collect.Streams;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

public abstract class TypeTraverser<T> {

    private final Class<T> clazz;

    public TypeTraverser(Class<T> clazz) {
        this.clazz = clazz;
    }

    protected abstract T whenTypeVar(@NonNull TypeVariable type, TypeMirror override);

    protected abstract T whenNonGenericType(@NonNull DeclaredType type, TypeMirror override);

    protected abstract T whenGenericType(@NonNull DeclaredType type, TypeMirror override, @NonNull T[] subtypes);

    @SuppressWarnings("unchecked")
    public T traverse(@NonNull TypeMirror type) {
        if (type.getKind() == TypeKind.TYPEVAR) {
            return whenTypeVar((TypeVariable) type, null);
        }

        if (!(type instanceof DeclaredType)) {
            throw new IllegalArgumentException("Type is not supported: " + type);
        }

        val declaredType = (DeclaredType) type;
        val typeArguments = declaredType.getTypeArguments();
        return typeArguments.size() == 0 ?
                whenNonGenericType(declaredType, null) :
                whenGenericType(
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

        if (!(type instanceof DeclaredType)) {
            throw new IllegalArgumentException("Type is not supported: " + type);
        }

        val declaredType = (DeclaredType) type;
        val typeArguments = declaredType.getTypeArguments();
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

    private boolean typeIsNonGeneric(int typeArgumentsSize, int typeOverrideSize) {
        if (typeArgumentsSize == 0) {
            if (typeOverrideSize > 0) {
                throw new IllegalArgumentException("Nongeneric type cannot be overridden by generic");
            }

            return true;
        }

        return false;
    }

    private boolean typeIsOverriddenByNonGeneric(int typeOverrideSize) {
        return typeOverrideSize == 0;
    }


    public static class TypeTreeNode {
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
