package com.strategyobject.substrateclient.rpc.codegen.encoder;

import com.squareup.javapoet.CodeBlock;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import com.strategyobject.substrateclient.common.codegen.TypeTraverser;
import com.strategyobject.substrateclient.common.types.Array;
import com.strategyobject.substrateclient.rpc.EncoderPair;
import com.strategyobject.substrateclient.rpc.RpcEncoder;
import com.strategyobject.substrateclient.rpc.RpcRegistryHelper;
import com.strategyobject.substrateclient.scale.ScaleRegistryHelper;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;
import lombok.val;
import lombok.var;

import javax.lang.model.type.*;
import java.util.Map;

import static com.strategyobject.substrateclient.rpc.codegen.Constants.*;

public class EncoderCompositor extends TypeTraverser<CodeBlock> {
    private final ProcessorContext context;
    private final Map<String, Integer> typeVarMap;
    private final String encoderAccessor;
    private final String writerAccessor;
    private final String writerMethod;
    private final String encoderRegistryVarName;
    private final String scaleRegistryVarName;
    private final TypeMirror arrayType;

    public EncoderCompositor(@NonNull ProcessorContext context,
                             @NonNull Map<String, Integer> typeVarMap,
                             @NonNull String encoderAccessor,
                             @NonNull String writerAccessor,
                             @NonNull String writerMethod,
                             @NonNull String encoderRegistryVarName,
                             @NonNull String scaleRegistryVarName) {
        super(CodeBlock.class);
        this.context = context;
        this.typeVarMap = typeVarMap;
        this.encoderAccessor = encoderAccessor;
        this.writerAccessor = writerAccessor;
        this.writerMethod = writerMethod;
        this.encoderRegistryVarName = encoderRegistryVarName;
        this.scaleRegistryVarName = scaleRegistryVarName;
        this.arrayType = context.erasure(context.getType(Array.class));
    }

    private CodeBlock getNonGenericCodeBlock(TypeMirror type) {
        return CodeBlock.builder()
                .add("$T.$L(($T) ", EncoderPair.class, PAIR_FACTORY_METHOD, RpcEncoder.class)
                .add("$L.resolve($T.class)", encoderRegistryVarName, type)
                .add(", ($T) ", ScaleWriter.class)
                .add("$L.resolve($T.class)", scaleRegistryVarName, type)
                .add(")")
                .build();
    }

    private CodeBlock getGenericCodeBlock(TypeMirror type, CodeBlock[] subtypes) {
        TypeMirror resolveType = context.erasure(type);
        val builder = CodeBlock.builder()
                .add("$T.$L(", EncoderPair.class, PAIR_FACTORY_METHOD)
                .add("$T.$L($L, $T.class, ",
                        RpcRegistryHelper.class,
                        RESOLVE_AND_INJECT_METHOD,
                        encoderRegistryVarName,
                        resolveType);

        for (var i = 0; i < subtypes.length; i++) {
            if (i > 0) builder.add(", ");
            builder.add(subtypes[i]);
        }

        builder.add("), $T.$L($L, $T.class, ",
                ScaleRegistryHelper.class,
                RESOLVE_AND_INJECT_METHOD,
                scaleRegistryVarName,
                resolveType);

        for (var i = 0; i < subtypes.length; i++) {
            if (i > 0) builder.add(", ");
            builder.add(subtypes[i]);
            builder.add(".$L", writerMethod);
        }

        return builder.add("))").build();
    }

    @Override
    protected CodeBlock whenWildcard(TypeMirror override) {
        return CodeBlock.builder()
                .add("$L.resolve($T.class)", encoderRegistryVarName, RPC_DISPATCH)
                .build();
    }

    @Override
    protected CodeBlock whenTypeVar(@NonNull TypeVariable type, TypeMirror _override) {
        return CodeBlock.builder()
                .add("$T.$L(($T) ", EncoderPair.class, PAIR_FACTORY_METHOD, RpcEncoder.class)
                .add(encoderAccessor, typeVarMap.get(type.toString())).add(", ($T) ", ScaleWriter.class)
                .add(writerAccessor, typeVarMap.get(type.toString()))
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
