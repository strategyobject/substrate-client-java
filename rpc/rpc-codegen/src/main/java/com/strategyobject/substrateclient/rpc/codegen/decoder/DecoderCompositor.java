package com.strategyobject.substrateclient.rpc.codegen.decoder;

import com.squareup.javapoet.CodeBlock;
import com.strategyobject.substrateclient.common.codegen.Constants;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import com.strategyobject.substrateclient.common.codegen.TypeTraverser;
import com.strategyobject.substrateclient.rpc.core.DecoderPair;
import com.strategyobject.substrateclient.rpc.core.RpcDecoder;
import com.strategyobject.substrateclient.rpc.core.RpcRegistryHelper;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.ScaleRegistryHelper;
import lombok.NonNull;
import lombok.var;

import javax.lang.model.type.*;
import java.util.Map;

import static com.strategyobject.substrateclient.rpc.codegen.Constants.PAIR_FACTORY_METHOD;
import static com.strategyobject.substrateclient.rpc.codegen.Constants.RESOLVE_AND_INJECT_METHOD;

public class DecoderCompositor extends TypeTraverser<CodeBlock> {
    private final ProcessorContext context;
    private final Map<String, Integer> typeVarMap;
    private final String decoderAccessor;
    private final String readerAccessor;
    private final String readerMethod;
    private final String decoderRegistryVarName;
    private final String scaleRegistryVarName;
    private final TypeMirror arrayType;

    public DecoderCompositor(@NonNull ProcessorContext context,
                             @NonNull Map<String, Integer> typeVarMap,
                             @NonNull String decoderAccessor,
                             @NonNull String readerAccessor,
                             @NonNull String readerMethod,
                             @NonNull String decoderRegistryVarName,
                             @NonNull String scaleRegistryVarName) {
        super(CodeBlock.class);

        this.context = context;
        this.typeVarMap = typeVarMap;
        this.decoderAccessor = decoderAccessor;
        this.readerAccessor = readerAccessor;
        this.readerMethod = readerMethod;
        this.decoderRegistryVarName = decoderRegistryVarName;
        this.scaleRegistryVarName = scaleRegistryVarName;
        this.arrayType = context.erasure(context.getType(Constants.ARRAY_TYPE));
    }

    private CodeBlock getTypeVarCodeBlock(TypeVariable type) {
        return CodeBlock.builder()
                .add("$T.$L(($T) ", DecoderPair.class, PAIR_FACTORY_METHOD, RpcDecoder.class)
                .add(decoderAccessor, typeVarMap.get(type.toString()))
                .add(", ($T) ", ScaleReader.class)
                .add(readerAccessor, typeVarMap.get(type.toString()))
                .add(")")
                .build();
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

    private CodeBlock getGenericCodeBlock(TypeMirror type, CodeBlock[] subtypes) {
        TypeMirror resolveType = context.erasure(type);

        var builder = CodeBlock.builder()
                .add("$T.$L(", DecoderPair.class, PAIR_FACTORY_METHOD)
                .add("$T.$L($T.class, ", RpcRegistryHelper.class, RESOLVE_AND_INJECT_METHOD, resolveType);
        for (var i = 0; i < subtypes.length; i++) {
            if (i > 0) builder.add(", ");
            builder.add(subtypes[i]);
        }

        builder.add("), $T.$L($T.class, ", ScaleRegistryHelper.class, RESOLVE_AND_INJECT_METHOD, resolveType);
        for (var i = 0; i < subtypes.length; i++) {
            if (i > 0) builder.add(", ");
            builder.add(subtypes[i]);
            builder.add(".$L", readerMethod);
        }
        builder.add(")");

        return builder
                .add(")").build();
    }

    @Override
    protected CodeBlock whenTypeVar(@NonNull TypeVariable type, TypeMirror _override) {
        return getTypeVarCodeBlock(type);
    }

    @Override
    protected CodeBlock whenPrimitiveType(@NonNull PrimitiveType type, TypeMirror _override) {
        return getNonGenericCodeBlock(type);
    }

    @Override
    protected CodeBlock whenNonGenericType(@NonNull DeclaredType type, TypeMirror _override) {
        return getNonGenericCodeBlock(type);
    }

    @Override
    protected CodeBlock whenGenericType(@NonNull DeclaredType type, TypeMirror _override, @NonNull CodeBlock[] subtypes) {
        return getGenericCodeBlock(type, subtypes);
    }

    @Override
    protected CodeBlock whenArrayPrimitiveType(@NonNull ArrayType type, TypeMirror _override) {
        return getNonGenericCodeBlock(type);
    }

    @Override
    protected CodeBlock whenArrayType(@NonNull ArrayType type, TypeMirror _override, @NonNull CodeBlock subtype) {
        return getGenericCodeBlock(arrayType, new CodeBlock[]{subtype});
    }
}
