package com.strategyobject.substrateclient.common.types.union;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.val;

import java.util.function.Function;

public class Union2<T0, T1> extends Union {
    public Union2() {
    }

    public <T> T match(@NonNull Function<T0, T> f0, @NonNull Function<T1, T> f1) {
        return index == 0 ? f0.apply(getItem0()) : f1.apply(getItem1());
    }

    @SuppressWarnings("unchecked")
    public T0 getItem0() {
        Preconditions.checkState(index == 0);
        return (T0) value;
    }

    @SuppressWarnings("unchecked")
    public T1 getItem1() {
        Preconditions.checkState(index == 1);
        return (T1) value;
    }

    public static <T0, T1> Union2<T0, T1> withItem0(T0 item0) {
        val result = new Union2<T0, T1>();
        result.value = item0;
        result.index = 0;
        return result;
    }

    public static <T0, T1> Union2<T0, T1> withItem1(T1 item1) {
        val result = new Union2<T0, T1>();
        result.value = item1;
        result.index = 1;
        return result;
    }
}
