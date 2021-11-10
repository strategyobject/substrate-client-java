package com.strategyobject.substrateclient.types.union;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.val;

import java.util.function.Function;

public class Union6<T0, T1, T2, T3, T4, T5> extends Union {
    private Union6() {
    }

    public <T> T match(@NonNull Function<T0, T> f0,
                       @NonNull Function<T1, T> f1,
                       @NonNull Function<T2, T> f2,
                       @NonNull Function<T3, T> f3,
                       @NonNull Function<T4, T> f4,
                       @NonNull Function<T5, T> f5) {
        switch (index) {
            case 0:
                return f0.apply(getItem0());
            case 1:
                return f1.apply(getItem1());
            case 2:
                return f2.apply(getItem2());
            case 3:
                return f3.apply(getItem3());
            case 4:
                return f4.apply(getItem4());
            default:
                return f5.apply(getItem5());
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

    @SuppressWarnings("unchecked")
    public T5 getItem5() {
        Preconditions.checkState(index == 5);
        return (T5) value;
    }

    public static <T0, T1, T2, T3, T4, T5> Union6<T0, T1, T2, T3, T4, T5> withItem0(T0 item0) {
        val result = new Union6<T0, T1, T2, T3, T4, T5>();
        result.value = item0;
        result.index = 0;
        return result;
    }

    public static <T0, T1, T2, T3, T4, T5> Union6<T0, T1, T2, T3, T4, T5> withItem1(T1 item1) {
        val result = new Union6<T0, T1, T2, T3, T4, T5>();
        result.value = item1;
        result.index = 1;
        return result;
    }

    public static <T0, T1, T2, T3, T4, T5> Union6<T0, T1, T2, T3, T4, T5> withItem2(T2 item2) {
        val result = new Union6<T0, T1, T2, T3, T4, T5>();
        result.value = item2;
        result.index = 2;
        return result;
    }

    public static <T0, T1, T2, T3, T4, T5> Union6<T0, T1, T2, T3, T4, T5> withItem3(T3 item3) {
        val result = new Union6<T0, T1, T2, T3, T4, T5>();
        result.value = item3;
        result.index = 3;
        return result;
    }

    public static <T0, T1, T2, T3, T4, T5> Union6<T0, T1, T2, T3, T4, T5> withItem4(T4 item4) {
        val result = new Union6<T0, T1, T2, T3, T4, T5>();
        result.value = item4;
        result.index = 4;
        return result;
    }

    public static <T0, T1, T2, T3, T4, T5> Union6<T0, T1, T2, T3, T4, T5> withItem5(T5 item5) {
        val result = new Union6<T0, T1, T2, T3, T4, T5>();
        result.value = item5;
        result.index = 5;
        return result;
    }
}
