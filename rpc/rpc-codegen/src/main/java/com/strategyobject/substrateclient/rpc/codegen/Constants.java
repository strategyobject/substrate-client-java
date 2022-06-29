package com.strategyobject.substrateclient.rpc.codegen;

import com.strategyobject.substrateclient.rpc.RpcDispatch;

public class Constants {
    public static final Class<?> RPC_DISPATCH = RpcDispatch.class;

    public static final String AUTO_REGISTER_TYPES_ARG = "types";
    public static final String PAIR_FACTORY_METHOD = "of";
    public static final String RESOLVE_AND_INJECT_METHOD = "resolveAndInjectOrNull";

    public static final String ENCODE_METHOD_NAME = "encode";
    public static final String DECODE_METHOD_NAME = "decode";

    public static final String TO_HEX = "toHexString";
    public static final String FROM_HEX_STRING = "fromHexString";

    public static final String ENCODER_UNSAFE_ACCESSOR = "getEncoder()";
    public static final String ENCODER_ACCESSOR = "getEncoderOrThrow()";
    public static final String DECODER_UNSAFE_ACCESSOR = "getDecoder()";
    public static final String DECODER_ACCESSOR = "getDecoderOrThrow()";

    public static final String WRITER_UNSAFE_ACCESSOR = "getScaleWriter()";
    public static final String WRITER_ACCESSOR = "getScaleWriterOrThrow()";
    public static final String READER_UNSAFE_ACCESSOR = "getScaleReader()";
    public static final String READER_ACCESSOR = "getScaleReaderOrThrow()";
    public static final String DECODERS_ARG = "decoders";
    public static final String ENCODERS_ARG = "encoders";

    public static final String ENCODER_REGISTRY = "encoderRegistry";
    public static final String DECODER_REGISTRY = "decoderRegistry";

    public static final String SCALE_WRITER_REGISTRY = "scaleWriterRegistry";
    public static final String SCALE_READER_REGISTRY = "scaleReaderRegistry";

    private Constants() {
    }
}
