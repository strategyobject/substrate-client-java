package com.strategyobject.substrateclient.rpc.codegen.encoder;

import com.squareup.javapoet.CodeBlock;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import com.strategyobject.substrateclient.common.codegen.TypeTraverser;
import com.strategyobject.substrateclient.rpc.core.EncoderPair;
import com.strategyobject.substrateclient.rpc.core.RpcEncoder;
import com.strategyobject.substrateclient.rpc.core.RpcRegistryHelper;
import com.strategyobject.substrateclient.scale.ScaleRegistryHelper;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;
import lombok.val;
import lombok.var;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import java.util.Map;

import static com.strategyobject.substrateclient.rpc.codegen.Constants.*;

public class EncoderCompositor extends TypeTraverser<CodeBlock> {
    private final ProcessorContext context;
    private final Map<String, Integer> typeVarMap;
    private final TypeMirror selfEncodable;
    private final String encoderAccessor;
    private final String writerAccessor;
    private final String writerMethod;
    private final String encoderRegistryVarName;
    private final String scaleRegistryVarName;

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
        this.selfEncodable = context.erasure(context.getType(RPC_SELF_ENCODABLE));
        this.encoderAccessor = encoderAccessor;
        this.writerAccessor = writerAccessor;
        this.writerMethod = writerMethod;
        this.encoderRegistryVarName = encoderRegistryVarName;
        this.scaleRegistryVarName = scaleRegistryVarName;
    }

    @Override
    protected CodeBlock whenTypeVar(@NonNull TypeVariable type, TypeMirror _override) {
        val builder = CodeBlock.builder()
                .add("$T.$L(($T) ", EncoderPair.class, PAIR_FACTORY_METHOD, RpcEncoder.class);

        if (context.isSubtypeOf(type, selfEncodable)) {
            builder.add("$L.resolve($T.class)", encoderRegistryVarName, selfEncodable);
        } else {
            builder.add(encoderAccessor, typeVarMap.get(type.toString()));
        }

        return builder.add(", ($T) ", ScaleWriter.class)
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

    private CodeBlock getNonGenericCodeBlock(TypeMirror type) {
        return CodeBlock.builder()
                .add("$T.$L(($T) ", EncoderPair.class, PAIR_FACTORY_METHOD, RpcEncoder.class)
                .add("$L.resolve($T.class)",
                        encoderRegistryVarName,
                        context.isSubtypeOf(type, selfEncodable) ?
                                selfEncodable :
                                type)
                .add(", ($T) ", ScaleWriter.class)
                .add("$L.resolve($T.class)", scaleRegistryVarName, type)
                .add(")")
                .build();
    }

    @Override
    protected CodeBlock whenGenericType(@NonNull DeclaredType type, TypeMirror _override, @NonNull CodeBlock[] subtypes) {
        TypeMirror resolveType = context.erasure(type);
        val builder = CodeBlock.builder()
                .add("$T.$L(", EncoderPair.class, PAIR_FACTORY_METHOD);

        if (context.isSubtypeOf(resolveType, selfEncodable)) {
            builder.add("($T) registry.resolve($T.class)", selfEncodable, RpcEncoder.class);
        } else {
            builder.add("$T.$L($T.class, ", RpcRegistryHelper.class, RESOLVE_AND_INJECT_METHOD, resolveType);
            for (var i = 0; i < subtypes.length; i++) {
                if (i > 0) builder.add(", ");
                builder.add(subtypes[i]);
            }
        }

        builder.add("), $T.$L($T.class, ", ScaleRegistryHelper.class, RESOLVE_AND_INJECT_METHOD, resolveType);
        for (var i = 0; i < subtypes.length; i++) {
            if (i > 0) builder.add(", ");
            builder.add(subtypes[i]);
            builder.add(".$L", writerMethod);
        }
        builder.add(")");

        return builder.add(")").build();
    }

    @Override
    protected boolean doTraverseArguments(@NonNull DeclaredType type, TypeMirror override) {
        return override != null || !context.isSubtypeOf(context.erasure(type), selfEncodable);
    }
}
