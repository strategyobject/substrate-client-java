package com.strategyobject.substrateclient.rpc.codegen.decoder;

import com.squareup.javapoet.CodeBlock;
import com.strategyobject.substrateclient.common.codegen.TypeTraverser;
import com.strategyobject.substrateclient.rpc.core.DecoderPair;
import com.strategyobject.substrateclient.rpc.core.RpcDecoder;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.var;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Types;
import java.util.Map;

import static com.strategyobject.substrateclient.rpc.codegen.Constants.PAIR_FACTORY_METHOD;

public class DecoderCompositor extends TypeTraverser<CodeBlock> {
    private final Types typeUtils;
    private final Map<String, Integer> typeVarMap;
    private final String decoderAccessor;
    private final String readerAccessor;
    private final String decoderRegistryVarName;
    private final String scaleRegistryVarName;

    public DecoderCompositor(@NonNull Types typeUtils,
                             @NonNull Map<String, Integer> typeVarMap,
                             @NonNull String decoderAccessor,
                             @NonNull String readerAccessor,
                             @NonNull String decoderRegistryVarName,
                             @NonNull String scaleRegistryVarName) {
        super(CodeBlock.class);
        this.typeUtils = typeUtils;
        this.typeVarMap = typeVarMap;
        this.decoderAccessor = decoderAccessor;
        this.readerAccessor = readerAccessor;
        this.decoderRegistryVarName = decoderRegistryVarName;
        this.scaleRegistryVarName = scaleRegistryVarName;
    }

    @Override
    protected CodeBlock whenTypeVar(@NonNull TypeVariable type, TypeMirror _override) {
        return CodeBlock.builder()
                .add("$T.$L(($T) ", DecoderPair.class, PAIR_FACTORY_METHOD, RpcDecoder.class)
                .add(decoderAccessor, typeVarMap.get(type.toString()))
                .add(", ($T) ", ScaleReader.class)
                .add(readerAccessor, typeVarMap.get(type.toString()))
                .add(")")
                .build();
    }

    @Override
    protected CodeBlock whenPrimitiveType(@NonNull PrimitiveType type, TypeMirror _override) {
        return getNonGenericCodeBlock(type);
    }

    @Override
    protected CodeBlock whenNonGenericType(@NonNull DeclaredType type, TypeMirror _override) {
        return getNonGenericCodeBlock(type);
    }

    private CodeBlock getNonGenericCodeBlock(TypeMirror type) {
        return CodeBlock.builder()
                .add("$T.$L(", DecoderPair.class, PAIR_FACTORY_METHOD)
                .add("($T) $L.resolve($T.class)", RpcDecoder.class, decoderRegistryVarName, type)
                .add(", ")
                .add("($T) $L.resolve($T.class)", ScaleReader.class, scaleRegistryVarName, type)
                .add(")")
                .build();
    }

    @Override
    protected CodeBlock whenGenericType(@NonNull DeclaredType type, TypeMirror _override, @NonNull CodeBlock[] subtypes) {
        TypeMirror resolveType = typeUtils.erasure(type);

        var builder = CodeBlock.builder()
                .add("$T.$L(", DecoderPair.class, PAIR_FACTORY_METHOD)
                .add("($T) $L.resolve($T.class).inject(", RpcDecoder.class, decoderRegistryVarName, resolveType);
        for (var i = 0; i < subtypes.length; i++) {
            if (i > 0) builder.add(", ");
            builder.add(subtypes[i]);
        }

        builder.add("), ($T) $L.resolve($T.class).inject(", ScaleReader.class, scaleRegistryVarName, resolveType);
        for (var i = 0; i < subtypes.length; i++) {
            if (i > 0) builder.add(", ");
            builder.add(subtypes[i]);
        }
        builder.add(")");

        return builder
                .add(")").build();
    }
}
