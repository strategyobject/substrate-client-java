package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.rpc.DecoderPair;
import com.strategyobject.substrateclient.rpc.annotation.AutoRegister;
import com.strategyobject.substrateclient.rpc.decoders.AbstractDecoder;
import com.strategyobject.substrateclient.rpc.registries.RpcDecoderRegistry;
import com.strategyobject.substrateclient.transport.RpcObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@AutoRegister(types = ExtrinsicStatus.class)
public class ExtrinsicStatusDecoder extends AbstractDecoder<ExtrinsicStatus> {
    private static final Map<String, ExtrinsicStatus> STATUS_TO_VALUE = new HashMap<>();
    private static final Map<String, Class<? extends ExtrinsicStatus>> STATUS_TO_CLASS = new HashMap<>();

    static {
        STATUS_TO_VALUE.put("future", ExtrinsicStatus.Future);
        STATUS_TO_VALUE.put("ready", ExtrinsicStatus.Ready);
        STATUS_TO_VALUE.put("dropped", ExtrinsicStatus.Dropped);
        STATUS_TO_VALUE.put("invalid", ExtrinsicStatus.Invalid);

        STATUS_TO_CLASS.put("broadcast", ExtrinsicStatus.Broadcast.class);
        STATUS_TO_CLASS.put("inBlock", ExtrinsicStatus.InBlock.class);
        STATUS_TO_CLASS.put("retracted", ExtrinsicStatus.Retracted.class);
        STATUS_TO_CLASS.put("finalityTimeout", ExtrinsicStatus.FinalityTimeout.class);
        STATUS_TO_CLASS.put("finalized", ExtrinsicStatus.Finalized.class);
        STATUS_TO_CLASS.put("usurped", ExtrinsicStatus.Usurped.class);
    }

    @Override
    protected ExtrinsicStatus decodeNonNull(RpcObject value, DecoderPair<?>[] decoders) {
        Optional<ExtrinsicStatus> decoded;
        if (value.isString()) {
            decoded = Optional.ofNullable(STATUS_TO_VALUE.get(value.asString()));
        } else if (value.isMap()) {
            decoded = (value.asMap()).entrySet().stream()
                    .filter(e -> STATUS_TO_CLASS.containsKey(e.getKey()))
                    .findFirst()
                    .map(e ->
                            (ExtrinsicStatus) RpcDecoderRegistry
                                    .resolve(STATUS_TO_CLASS.get(e.getKey()))
                                    .decode(value)
                    );
        } else {
            decoded = Optional.empty();
        }

        return decoded.orElseThrow(this::unknownStatus);
    }

    private RuntimeException unknownStatus() {
        return new RuntimeException("Unknown extrinsic status.");
    }
}
