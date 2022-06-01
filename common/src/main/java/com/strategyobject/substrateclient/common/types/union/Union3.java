package com.strategyobject.substrateclient.common.types.union;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.val;

import java.util.function.Function;

public class Union3<T0, T1, T2> extends Union {
    private Union3() {
    }

    public <T> T match(@NonNull Function<T0, T> f0,
                       @NonNull Function<T1, T> f1,
                       @NonNull Function<T2, T> f2) {
        switch (index) {
            case 0:
                return f0.apply(getItem0());
            case 1:
                return f1.apply(getItem1());
            default:
                return f2.apply(getItem2());
        }
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

    @SuppressWarnings("unchecked")
    public T2 getItem2() {
        Preconditions.checkState(index == 2);
        return (T2) value;
    }

    public static <T0, T1, T2> Union3<T0, T1, T2> withItem0(T0 item0) {
        val result = new Union3<T0, T1, T2>();
        result.value = item0;
        result.index = 0;
        return result;
    }

    public static <T0, T1, T2> Union3<T0, T1, T2> withItem1(T1 item1) {
        val result = new Union3<T0, T1, T2>();
        result.value = item1;
        result.index = 1;
        return result;
    }

    public static <T0, T1, T2> Union3<T0, T1, T2> withItem2(T2 item2) {
        val result = new Union3<T0, T1, T2>();
        result.value = item2;
        result.index = 2;
        return result;
    }
}
