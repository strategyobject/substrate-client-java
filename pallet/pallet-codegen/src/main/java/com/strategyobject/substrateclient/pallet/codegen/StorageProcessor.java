package com.strategyobject.substrateclient.pallet.codegen;

import com.google.common.base.Strings;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.strategyobject.substrateclient.common.codegen.AnnotationUtils;
import com.strategyobject.substrateclient.common.codegen.ProcessingException;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import com.strategyobject.substrateclient.common.codegen.TypeTraverser;
import com.strategyobject.substrateclient.common.types.FixedBytes;
import com.strategyobject.substrateclient.common.types.Size;
import com.strategyobject.substrateclient.pallet.annotation.Storage;
import com.strategyobject.substrateclient.pallet.annotation.StorageHasher;
import com.strategyobject.substrateclient.pallet.storage.*;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.codegen.ScaleAnnotationParser;
import com.strategyobject.substrateclient.scale.codegen.reader.ReaderCompositor;
import com.strategyobject.substrateclient.scale.codegen.writer.WriterCompositor;
import lombok.NonNull;
import lombok.val;
import lombok.var;

import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;

import static com.strategyobject.substrateclient.common.codegen.AnnotationUtils.suppressWarnings;
import static com.strategyobject.substrateclient.pallet.codegen.Constants.*;

class StorageProcessor extends PalletMethodProcessor {
    private static final String VALUE_READER = "valueReader";
    private static final String KEY_PROVIDER = "keyProvider";
    private static final String STORAGE_MAP = "storage";
    private static final String HASHERS = "hashers";
    private static final String INITIALIZER_SUFFIX = "Initializer";

    public StorageProcessor(@NonNull TypeElement palletElement) {
        super(palletElement);
    }

    @Override
    void process(@NonNull String palletName,
                 @NonNull ExecutableElement method,
                 TypeSpec.@NonNull Builder typeSpecBuilder,
                 MethodSpec.Builder constructorBuilder,
                 @NonNull ProcessorContext context) throws ProcessingException {
        val annotation = AnnotationUtils.getAnnotationMirror(method, Storage.class);
        if (annotation == null) {
            return;
        }

        validate(method, annotation, context);

        createBackField(typeSpecBuilder, method);
        typeSpecBuilder.addMethod(backFieldInitializer(palletName, method, annotation, context));
        assignBackFieldInConstructor(constructorBuilder, method);
        typeSpecBuilder.addMethod(publicMethod(method));
    }

    private void createBackField(TypeSpec.Builder typeSpecBuilder, ExecutableElement method) {
        typeSpecBuilder.addField(
                TypeName.get(method.getReturnType()),
                method.getSimpleName().toString(),
                Modifier.FINAL, Modifier.PRIVATE
        );
    }

    private void assignBackFieldInConstructor(MethodSpec.Builder constructorBuilder, ExecutableElement method) {
        constructorBuilder.addStatement("this.$1L = $1L$2L()",
                method.getSimpleName().toString(),
                INITIALIZER_SUFFIX);
    }

    private MethodSpec publicMethod(ExecutableElement method) {
        val returnType = method.getReturnType();

        return MethodSpec.methodBuilder(method.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(TypeName.get(returnType))
                .addStatement("return this.$L", method.getSimpleName().toString())
                .build();
    }

    private MethodSpec backFieldInitializer(String palletName,
                                            ExecutableElement method,
                                            AnnotationMirror storageAnnotation,
                                            ProcessorContext context) throws ProcessingException {
        val returnType = method.getReturnType();
        validateMethodSignature(method, returnType, context);

        val valueType = ((DeclaredType) returnType).getTypeArguments().get(0);

        val methodSpecBuilder =
                MethodSpec.methodBuilder(method.getSimpleName().toString() + INITIALIZER_SUFFIX)
                        .addModifiers(Modifier.PRIVATE)
                        .addAnnotation(suppressWarnings("unchecked", "rawtypes"))
                        .returns(TypeName.get(returnType));

        val scaleAnnotationParser = new ScaleAnnotationParser(context);
        val readerCompositor = ReaderCompositor.disallowOpenGeneric(context);
        val writerCompositor = WriterCompositor.disallowOpenGeneric(context);

        assignStorageMapImpl(methodSpecBuilder,
                palletName,
                method,
                storageAnnotation,
                valueType,
                scaleAnnotationParser,
                readerCompositor,
                writerCompositor,
                context);

        methodSpecBuilder
                .addStatement("return $L", STORAGE_MAP);


        return methodSpecBuilder.build();
    }

    private void assignStorageMapImpl(MethodSpec.Builder methodSpecBuilder,
                                      String palletName,
                                      ExecutableElement method,
                                      AnnotationMirror storageAnnotation,
                                      TypeMirror valueType,
                                      ScaleAnnotationParser scaleAnnotationParser,
                                      ReaderCompositor readerCompositor,
                                      WriterCompositor writerCompositor,
                                      ProcessorContext context) throws ProcessingException {
        assignValueReader(methodSpecBuilder,
                method,
                valueType,
                scaleAnnotationParser,
                readerCompositor);

        assignKeyProvider(methodSpecBuilder,
                method,
                palletName,
                storageAnnotation,
                scaleAnnotationParser,
                readerCompositor,
                writerCompositor,
                context);

        methodSpecBuilder
                .addStatement("$1T $2L = $1T.$3L($4L, $5L, $6L)",
                        StorageNMapImpl.class,
                        STORAGE_MAP,
                        STORAGE_FACTORY_METHOD,
                        STATE,
                        VALUE_READER,
                        KEY_PROVIDER);
    }

    private void assignKeyProvider(MethodSpec.Builder methodSpecBuilder,
                                   ExecutableElement method,
                                   String palletName,
                                   AnnotationMirror storageAnnotation,
                                   ScaleAnnotationParser scaleAnnotationParser,
                                   ReaderCompositor readerCompositor,
                                   WriterCompositor writerCompositor,
                                   ProcessorContext context) throws ProcessingException {
        val storageName = AnnotationUtils.<String>getValueFromAnnotation(storageAnnotation,
                "value");

        methodSpecBuilder.addStatement("$1T $2L = $1T.$3L($4S, $5S)",
                StorageKeyProvider.class,
                KEY_PROVIDER,
                STORAGE_KEY_PROVIDER_FACTORY_METHOD,
                palletName,
                storageName);

        val keys = AnnotationUtils
                .<List<AnnotationMirror>>getValueFromAnnotation(storageAnnotation,
                        "keys");

        if (keys != null && keys.size() > 0) {
            methodSpecBuilder.addStatement("$1T[] $2L = new $1T[$3L]",
                    KeyHasher.class,
                    HASHERS,
                    keys.size());

            for (var i = 0; i < keys.size(); i++) {
                putHasher(methodSpecBuilder,
                        method,
                        storageName,
                        context,
                        keys.get(i),
                        scaleAnnotationParser,
                        readerCompositor,
                        writerCompositor,
                        CodeBlock
                                .builder()
                                .add("$L[$L]", HASHERS, i)
                                .build());
            }

            methodSpecBuilder.addStatement("$L.$L($L)",
                    KEY_PROVIDER,
                    STORAGE_KEY_PROVIDER_ADD_HASHERS,
                    HASHERS);
        }
    }

    private void putHasher(MethodSpec.Builder methodSpecBuilder,
                           ExecutableElement method,
                           String storageName,
                           ProcessorContext context,
                           AnnotationMirror keyAnnotation,
                           ScaleAnnotationParser scaleAnnotationParser,
                           ReaderCompositor readerCompositor,
                           WriterCompositor writerCompositor,
                           CodeBlock hasher) throws ProcessingException {
        val type = AnnotationUtils.<AnnotationMirror>getValueFromAnnotation(keyAnnotation, "type");
        val typeOverride = type != null
                ? scaleAnnotationParser.parse(type)
                : scaleAnnotationParser.parse(
                Objects.requireNonNull(AnnotationUtils.<AnnotationMirror>getValueFromAnnotation(keyAnnotation, "generic")));
        val keySize = determineKeySize(context, typeOverride);
        val readerCode = readerCompositor.traverse(typeOverride);
        val writerCode = writerCompositor.traverse(typeOverride);

        methodSpecBuilder
                .addStatement(
                        CodeBlock.builder()
                                .add(hasher)
                                .add(" = $T.$L(($T)",
                                        KeyHasher.class,
                                        KEY_HASHER_FACTORY_METHOD,
                                        ScaleWriter.class)
                                .add(writerCode)
                                .add(", ($T)", ScaleReader.class)
                                .add(readerCode)
                                .add(", ")
                                .add(resolveHashingAlgorithm(keyAnnotation,
                                        keySize,
                                        storageName,
                                        method))
                                .add(")")
                                .build());
    }

    private int determineKeySize(ProcessorContext context,
                                 TypeTraverser.TypeTreeNode typeOverride) {
        if (context.isSubtype(typeOverride.getType(), context.erasure(context.getType(FixedBytes.class)))) {
            val fixedBytes = ((TypeElement) ((DeclaredType) typeOverride.getType())
                    .asElement())
                    .getSuperclass();
            val size = ((DeclaredType) fixedBytes).getTypeArguments().get(0);

            val zero = context.getType(Size.Zero.class);
            if (context.isAssignable(size, zero)) {
                return Size.zero.getValue();
            }

            val of32 = context.getType(Size.Of32.class);
            if (context.isAssignable(size, of32)) {
                return Size.of32.getValue();
            }

            val of64 = context.getType(Size.Of64.class);
            if (context.isAssignable(size, of64)) {
                return Size.of64.getValue();
            }

            val of96 = context.getType(Size.Of96.class);
            if (context.isAssignable(size, of96)) {
                return Size.of96.getValue();
            }

            val of128 = context.getType(Size.Of128.class);
            if (context.isAssignable(size, of128)) {
                return Size.of128.getValue();
            }
        }

        return -1;
    }

    private CodeBlock resolveHashingAlgorithm(AnnotationMirror keyAnnotation,
                                              int keySize,
                                              String storageAnnotation,
                                              ExecutableElement method) throws ProcessingException {
        val builder = CodeBlock.builder();
        val hasher = AnnotationUtils.<VariableElement>getValueFromAnnotation(keyAnnotation, "hasher");
        val hasherName = Objects.requireNonNull(hasher).getSimpleName().toString();

        if (hasherName.equals(StorageHasher.BLAKE2_128_CONCAT.toString())) {
            builder.add("$T.$L()",
                    Blake2B128Concat.class,
                    BLAKE_2_128_CONCAT_INSTANCE);
        }

        if (hasherName.equals(StorageHasher.TWOX_64_CONCAT.toString())) {
            builder.add("$T.$L()",
                    TwoX64Concat.class,
                    TWO_X64_CONCAT_INSTANCE);
        }

        if (hasherName.equals(StorageHasher.IDENTITY.toString())) {
            if (keySize < 0) {
                throw new ProcessingException(
                        palletElement,
                        "`@%s` of `%s.%s` contains an incorrect type of key or a hashing algorithm. " +
                                "`%s` can only be applied to a key with fixed size.",
                        storageAnnotation.getClass().getSimpleName(),
                        palletElement.getQualifiedName().toString(),
                        StorageHasher.IDENTITY.toString(),
                        method.getSimpleName());
            }

            builder.add("$T.$L()",
                    Identity.class,
                    IDENTITY_INSTANCE);
        }

        return builder.build();
    }

    private void assignValueReader(MethodSpec.Builder methodSpecBuilder,
                                   ExecutableElement method,
                                   TypeMirror valueType,
                                   ScaleAnnotationParser scaleAnnotationParser,
                                   ReaderCompositor readerCompositor) {
        val typeOverride = scaleAnnotationParser.parse(method);
        val readerCode = typeOverride != null ?
                readerCompositor.traverse(valueType, typeOverride) :
                readerCompositor.traverse(valueType);

        methodSpecBuilder
                .addStatement(CodeBlock.builder()
                        .add("$T $L = ", ScaleReader.class, VALUE_READER)
                        .add(readerCode)
                        .build());
    }

    private void validateMethodSignature(ExecutableElement method,
                                         TypeMirror returnType,
                                         ProcessorContext context) throws ProcessingException {
        val expectedReturnType = context.erasure(
                context.getType(StorageNMap.class));
        if (!context.isSameType(expectedReturnType, context.erasure(returnType))) {
            throw new ProcessingException(
                    palletElement,
                    "Method `%s.%s` has unexpected return type. Must be `%s`.",
                    palletElement.getQualifiedName().toString(),
                    method.getSimpleName(),
                    StorageNMap.class.getSimpleName());
        }

        if (!method.getParameters().isEmpty()) {
            throw new ProcessingException(
                    palletElement,
                    "Method `%s.%s` mustn't have parameters.",
                    palletElement.getQualifiedName().toString(),
                    method.getSimpleName());
        }
    }

    protected void validate(ExecutableElement method, AnnotationMirror storageAnnotation, ProcessorContext context)
            throws ProcessingException {
        ensureNameIsSet(method, storageAnnotation);
        validateKeys(method, storageAnnotation);

        val returnType = method.getReturnType();
        if (!context.isSameType(context.erasure(returnType), context.erasure(context.getType(StorageNMap.class)))) {
            throw new ProcessingException(
                    palletElement,
                    "`Method `%s.%s` has incorrect return type." +
                            "Must be `%s`.",
                    palletElement.getQualifiedName().toString(),
                    method.getSimpleName(),
                    StorageNMap.class.getSimpleName());
        }
    }

    private void validateKeys(ExecutableElement method, AnnotationMirror storageAnnotation) throws ProcessingException {
        val keys = AnnotationUtils.<List<AnnotationMirror>>getValueFromAnnotation(storageAnnotation,
                "keys");
        if (keys == null) {
            return;
        }

        for (val key : keys) {
            val type = AnnotationUtils.<AnnotationMirror>getValueFromAnnotation(key, "type");
            val generic = AnnotationUtils.getValueFromAnnotation(key, "generic");
            if (type == null && generic == null) {
                throw new ProcessingException(
                        palletElement,
                        "`@%s` of `%s.%s` isn't adjusted correctly. " +
                                "Please set either `type` or `generic` parameter of @StorageKey.",
                        storageAnnotation.getClass().getSimpleName(),
                        palletElement.getQualifiedName().toString(),
                        method.getSimpleName());
            }

            if (type != null && generic != null) {
                throw new ProcessingException(
                        palletElement,
                        "`@%s` of `%s.%s` isn't adjusted correctly. " +
                                "Ambiguous scale type of key. " +
                                "Please set only one parameter of @StorageKey: `type` or `generic`.",
                        storageAnnotation.getClass().getSimpleName(),
                        palletElement.getQualifiedName().toString(),
                        method.getSimpleName());
            }
        }
    }

    private void ensureNameIsSet(ExecutableElement method, AnnotationMirror storageAnnotation) throws ProcessingException {
        val name = AnnotationUtils.<String>getValueFromAnnotation(storageAnnotation,
                "value");
        if (Strings.isNullOrEmpty(name)) {
            throw new ProcessingException(
                    palletElement,
                    "`@%s` of `%s.%s` doesn't have `value`.",
                    Storage.class.getSimpleName(),
                    palletElement.getQualifiedName().toString(),
                    method.getSimpleName());
        }
    }
}
