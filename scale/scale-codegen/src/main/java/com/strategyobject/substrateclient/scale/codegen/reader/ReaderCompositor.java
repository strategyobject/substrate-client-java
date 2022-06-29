package com.strategyobject.substrateclient.scale.codegen.reader;

import com.google.common.base.Strings;
import com.squareup.javapoet.CodeBlock;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import com.strategyobject.substrateclient.common.codegen.TypeNotSupportedException;
import com.strategyobject.substrateclient.common.codegen.TypeTraverser;
import com.strategyobject.substrateclient.common.types.Array;
import lombok.NonNull;
import lombok.var;

import javax.lang.model.type.*;
import java.util.Map;

public class ReaderCompositor extends TypeTraverser<CodeBlock> {
    private final ProcessorContext context;
    private final Map<String, Integer> typeVarMap;
    private final String readerAccessor;
    private final String registryVarName;
    private final TypeMirror arrayType;

    private ReaderCompositor(ProcessorContext context,
                             Map<String, Integer> typeVarMap,
                             String readerAccessor,
                             String registryVarName) {
        super(CodeBlock.class);

        this.context = context;
        this.typeVarMap = typeVarMap;
        this.readerAccessor = readerAccessor;
        this.registryVarName = registryVarName;
        this.arrayType = context.erasure(context.getType(Array.class));
    }

    public static ReaderCompositor forAnyType(@NonNull ProcessorContext context,
                                              @NonNull Map<String, Integer> typeVarMap,
                                              @NonNull String readerAccessor,
                                              @NonNull String registryVarName) {
        return new ReaderCompositor(context, typeVarMap, readerAccessor, registryVarName);
    }

    public static ReaderCompositor disallowOpenGeneric(@NonNull ProcessorContext context,
                                                       @NonNull String registryVarName) {
        return new ReaderCompositor(context, null, null, registryVarName);
    }

    private CodeBlock getTypeVarCodeBlock(TypeVariable type) {
        if (Strings.isNullOrEmpty(readerAccessor)) {
            throw new IllegalStateException("The compositor doesn't support open generics.");
        }

        return CodeBlock.builder()
                .add(readerAccessor, typeVarMap.get(type.toString()))
                .build();
    }

    private CodeBlock getNonGenericCodeBlock(TypeMirror type, TypeMirror override) {
        return CodeBlock.builder()
                .add("$L.resolve($T.class)", registryVarName, override != null ? override : type)
                .build();
    }

    private CodeBlock getGenericCodeBlock(TypeMirror type, TypeMirror override, CodeBlock[] subtypes) {
        TypeMirror resolveType;
        if (override != null) {
            if (context.isNonGeneric(override)) {
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


    @Override
    protected CodeBlock whenWildcard(TypeMirror override) {
        throw new TypeNotSupportedException("WILDCARD");
    }

    @Override
    protected CodeBlock whenTypeVar(@NonNull TypeVariable type, TypeMirror _override) {
        return getTypeVarCodeBlock(type);
    }

    @Override
    protected CodeBlock whenPrimitiveType(@NonNull PrimitiveType type, TypeMirror override) {
        return getNonGenericCodeBlock(type, override);
    }

    @Override
    protected CodeBlock whenNonGenericType(@NonNull DeclaredType type, TypeMirror override) {
        return getNonGenericCodeBlock(type, override);
    }

    @Override
    protected CodeBlock whenGenericType(@NonNull DeclaredType type, TypeMirror override, @NonNull CodeBlock[] subtypes) {
        return getGenericCodeBlock(type, override, subtypes);
    }

    @Override
    protected CodeBlock whenArrayPrimitiveType(@NonNull ArrayType type, TypeMirror override) {
        return getNonGenericCodeBlock(type, override);
    }

    @Override
    protected CodeBlock whenArrayType(@NonNull ArrayType type, TypeMirror override, @NonNull CodeBlock subtype) {
        return getGenericCodeBlock(arrayType, override, new CodeBlock[]{subtype});
    }
}
