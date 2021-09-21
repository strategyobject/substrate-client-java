package com.strategyobject.substrateclient.scale.codegen.reader;

import com.squareup.javapoet.CodeBlock;
import com.strategyobject.substrateclient.common.codegen.TypeTraverser;
import com.strategyobject.substrateclient.scale.codegen.ProcessorContext;
import lombok.NonNull;
import lombok.var;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import java.util.Map;

public class TypeReadGenerator extends TypeTraverser<CodeBlock> {
    private final ProcessorContext context;
    private final Map<String, Integer> typeVarMap;

    public TypeReadGenerator(Class<CodeBlock> clazz, ProcessorContext context, Map<String, Integer> typeVarMap) {
        super(clazz);
        this.context = context;
        this.typeVarMap = typeVarMap;
    }

    @Override
    protected CodeBlock whenTypeVar(@NonNull TypeVariable type, TypeMirror _override) {
        return CodeBlock.builder()
                .add("readers[$L]", typeVarMap.get(type.toString()))
                .build();
    }

    @Override
    protected CodeBlock whenNonGenericType(@NonNull DeclaredType type, TypeMirror override) {
        return CodeBlock.builder()
                .add("registry.resolve($T.class)", override != null ? override : type)
                .build();
    }

    @Override
    protected CodeBlock whenGenericType(@NonNull DeclaredType type, TypeMirror override, @NonNull CodeBlock[] subtypes) {
        TypeMirror resolveType;
        if (override != null) {
            if (!context.isGeneric(override)) {
                return CodeBlock.builder()
                        .add("registry.resolve($T.class)", override)
                        .build();
            }

            resolveType = override;
        } else {
            resolveType = context.getTypeUtils().erasure(type);
        }

        var builder = CodeBlock.builder().add("registry.resolve($T.class).inject(", resolveType);
        for (var i = 0; i < subtypes.length; i++) {
            if (i > 0) builder.add(", ");
            builder.add(subtypes[i]);
        }

        return builder.add(")").build();
    }
}
