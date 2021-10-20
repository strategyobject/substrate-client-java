package com.strategyobject.substrateclient.scale.codegen.reader;

import com.squareup.javapoet.CodeBlock;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import com.strategyobject.substrateclient.common.codegen.TypeTraverser;
import lombok.NonNull;
import lombok.var;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import java.util.Map;

public class ReaderCompositor extends TypeTraverser<CodeBlock> {
    private final ProcessorContext context;
    private final Map<String, Integer> typeVarMap;
    private final String readerAccessor;
    private final String registryVarName;

    public ReaderCompositor(@NonNull ProcessorContext context,
                            @NonNull Map<String, Integer> typeVarMap,
                            @NonNull String readerAccessor,
                            @NonNull String registryVarName) {
        super(CodeBlock.class);
        this.context = context;
        this.typeVarMap = typeVarMap;
        this.readerAccessor = readerAccessor;
        this.registryVarName = registryVarName;
    }

    @Override
    protected CodeBlock whenTypeVar(@NonNull TypeVariable type, TypeMirror _override) {
        return CodeBlock.builder()
                .add(readerAccessor, typeVarMap.get(type.toString()))
                .build();
    }

    @Override
    protected CodeBlock whenPrimitiveType(@NonNull PrimitiveType type, TypeMirror override) {
        return getNonGenericCodeBlock(type, override);
    }

    @Override
    protected CodeBlock whenNonGenericType(@NonNull DeclaredType type, TypeMirror override) {
        return getNonGenericCodeBlock(type, override);
    }

    private CodeBlock getNonGenericCodeBlock(TypeMirror type, TypeMirror override) {
        return CodeBlock.builder()
                .add("$L.resolve($T.class)", registryVarName, override != null ? override : type)
                .build();
    }

    @Override
    protected CodeBlock whenGenericType(@NonNull DeclaredType type, TypeMirror override, @NonNull CodeBlock[] subtypes) {
        TypeMirror resolveType;
        if (override != null) {
            if (!context.isGeneric(override)) {
                return CodeBlock.builder()
                        .add("$L.resolve($T.class)", registryVarName, override)
                        .build();
            }

            resolveType = override;
        } else {
            resolveType = context.erasure(type);
        }

        var builder = CodeBlock.builder().add("$L.resolve($T.class).inject(", registryVarName, resolveType);
        for (var i = 0; i < subtypes.length; i++) {
            if (i > 0) builder.add(", ");
            builder.add(subtypes[i]);
        }

        return builder.add(")").build();
    }
}
