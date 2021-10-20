package com.strategyobject.substrateclient.scale.codegen.writer;

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

import static com.strategyobject.substrateclient.scale.codegen.ScaleProcessorHelper.SCALE_SELF_WRITABLE;

public class WriterCompositor extends TypeTraverser<CodeBlock> {
    private final ProcessorContext context;
    private final Map<String, Integer> typeVarMap;
    private final TypeMirror selfWritable;
    private final String writerAccessor;
    private final String registryVarName;

    public WriterCompositor(ProcessorContext context,
                            Map<String, Integer> typeVarMap,
                            String writerAccessor,
                            String registryVarName) {
        super(CodeBlock.class);
        this.context = context;
        this.typeVarMap = typeVarMap;
        this.selfWritable = context.erasure(context.getType(SCALE_SELF_WRITABLE));
        this.writerAccessor = writerAccessor;
        this.registryVarName = registryVarName;
    }

    @Override
    protected CodeBlock whenTypeVar(@NonNull TypeVariable type, TypeMirror _override) {
        return context.isSubtypeOf(type, selfWritable) ?
                CodeBlock.builder()
                        .add("$L.resolve($T.class)", registryVarName, selfWritable)
                        .build() :
                CodeBlock.builder()
                        .add(writerAccessor, typeVarMap.get(type.toString()))
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
                .add("$L.resolve($T.class)",
                        registryVarName,
                        override != null ?
                                override :
                                context.isSubtypeOf(type, selfWritable) ?
                                        selfWritable :
                                        type)
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

            if (context.isSubtypeOf(resolveType, selfWritable)) {
                return CodeBlock.builder().add("$L.resolve($T.class)", registryVarName, selfWritable).build();
            }
        }

        var builder = CodeBlock.builder().add("$L.resolve($T.class).inject(", registryVarName, resolveType);
        for (var i = 0; i < subtypes.length; i++) {
            if (i > 0) builder.add(", ");
            builder.add(subtypes[i]);
        }

        return builder.add(")").build();
    }

    @Override
    protected boolean doTraverseArguments(@NonNull DeclaredType type, TypeMirror override) {
        return override != null || !context.isSubtypeOf(context.erasure(type), selfWritable);
    }
}
