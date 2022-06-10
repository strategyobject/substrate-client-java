package com.strategyobject.substrateclient.common.types.union;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.val;

import java.util.function.Function;

public class Union5<T0, T1, T2, T3, T4> extends Union {
    private Union5() {
    }

    public <T> T match(@NonNull Function<T0, T> f0,
                       @NonNull Function<T1, T> f1,
                       @NonNull Function<T2, T> f2,
                       @NonNull Function<T3, T> f3,
                       @NonNull Function<T4, T> f4) {
        switch (index) {
            case 0:
                return f0.apply(getItem0());
            case 1:
                return f1.apply(getItem1());
            case 2:
                return f2.apply(getItem2());
            case 3:
                return f3.apply(getItem3());
            default:
                return f4.apply(getItem4());
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

    @SuppressWarnings("unchecked")
    public T3 getItem3() {
        Preconditions.checkState(index == 3);
        return (T3) value;
    }

    @SuppressWarnings("unchecked")
    public T4 getItem4() {
        Preconditions.checkState(index == 4);
        return (T4) value;
    }

    public static <T0, T1, T2, T3, T4> Union5<T0, T1, T2, T3, T4> withItem0(T0 item0) {
        val result = new Union5<T0, T1, T2, T3, T4>();
        result.value = item0;
        result.index = 0;
        return result;
    }

    public static <T0, T1, T2, T3, T4> Union5<T0, T1, T2, T3, T4> withItem1(T1 item1) {
        val result = new Union5<T0, T1, T2, T3, T4>();
        result.value = item1;
        result.index = 1;
        return result;
    }

    public static <T0, T1, T2, T3, T4> Union5<T0, T1, T2, T3, T4> withItem2(T2 item2) {
        val result = new Union5<T0, T1, T2, T3, T4>();
        result.value = item2;
        result.index = 2;
        return result;
    }

    public static <T0, T1, T2, T3, T4> Union5<T0, T1, T2, T3, T4> withItem3(T3 item3) {
        val result = new Union5<T0, T1, T2, T3, T4>();
        result.value = item3;
        result.index = 3;
        return result;
    }

    public static <T0, T1, T2, T3, T4> Union5<T0, T1, T2, T3, T4> withItem4(T4 item4) {
        val result = new Union5<T0, T1, T2, T3, T4>();
        result.value = item4;
        result.index = 4;
        return result;
    }
}
