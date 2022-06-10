package com.strategyobject.substrateclient.common.types.union;

import lombok.NonNull;
import lombok.val;

import java.util.function.Function;

public class Union1<T0> extends Union {
    private Union1() {
    }

    public <T> T match(@NonNull Function<T0, T> f0) {
        return f0.apply(getItem0());
    }

    @SuppressWarnings("unchecked")
    public T0 getItem0() {
        return (T0) value;
    }

    public static <T0> Union1<T0> withItem0(T0 item0) {
        val result = new Union1<T0>();
        result.value = item0;
        result.index = 0;
        return result;
    }
}