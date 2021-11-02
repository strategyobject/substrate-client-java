package com.strategyobject.substrateclient.types.union;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.val;

import java.util.function.Function;

public class Union11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> extends Union {
    private Union11() {
    }

    public <T> T match(@NonNull Function<T0, T> f0,
                       @NonNull Function<T1, T> f1,
                       @NonNull Function<T2, T> f2,
                       @NonNull Function<T3, T> f3,
                       @NonNull Function<T4, T> f4,
                       @NonNull Function<T5, T> f5,
                       @NonNull Function<T6, T> f6,
                       @NonNull Function<T7, T> f7,
                       @NonNull Function<T8, T> f8,
                       @NonNull Function<T9, T> f9,
                       @NonNull Function<T10, T> f10) {
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
            case 5:
                return f5.apply(getItem5());
            case 6:
                return f6.apply(getItem6());
            case 7:
                return f7.apply(getItem7());
            case 8:
                return f8.apply(getItem8());
            case 9:
                return f9.apply(getItem9());
            default:
                return f10.apply(getItem10());
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

    @SuppressWarnings("unchecked")
    public T6 getItem6() {
        Preconditions.checkState(index == 6);
        return (T6) value;
    }

    @SuppressWarnings("unchecked")
    public T7 getItem7() {
        Preconditions.checkState(index == 7);
        return (T7) value;
    }

    @SuppressWarnings("unchecked")
    public T8 getItem8() {
        Preconditions.checkState(index == 8);
        return (T8) value;
    }

    @SuppressWarnings("unchecked")
    public T9 getItem9() {
        Preconditions.checkState(index == 9);
        return (T9) value;
    }

    @SuppressWarnings("unchecked")
    public T10 getItem10() {
        Preconditions.checkState(index == 10);
        return (T10) value;
    }

    public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Union11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> withItem0(T0 item0) {
        val result = new Union11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>();
        result.value = item0;
        result.index = 0;
        return result;
    }

    public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Union11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> withItem1(T1 item1) {
        val result = new Union11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>();
        result.value = item1;
        result.index = 1;
        return result;
    }

    public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Union11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> withItem2(T2 item2) {
        val result = new Union11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>();
        result.value = item2;
        result.index = 2;
        return result;
    }

    public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Union11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> withItem3(T3 item3) {
        val result = new Union11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>();
        result.value = item3;
        result.index = 3;
        return result;
    }

    public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Union11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> withItem4(T4 item4) {
        val result = new Union11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>();
        result.value = item4;
        result.index = 4;
        return result;
    }

    public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Union11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> withItem5(T5 item5) {
        val result = new Union11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>();
        result.value = item5;
        result.index = 5;
        return result;
    }

    public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Union11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> withItem6(T6 item6) {
        val result = new Union11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>();
        result.value = item6;
        result.index = 6;
        return result;
    }

    public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Union11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> withItem7(T7 item7) {
        val result = new Union11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>();
        result.value = item7;
        result.index = 7;
        return result;
    }

    public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Union11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> withItem8(T8 item8) {
        val result = new Union11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>();
        result.value = item8;
        result.index = 8;
        return result;
    }

    public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Union11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> withItem9(T9 item9) {
        val result = new Union11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>();
        result.value = item9;
        result.index = 9;
        return result;
    }

    public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Union11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> withItem10(T10 item10) {
        val result = new Union11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>();
        result.value = item10;
        result.index = 10;
        return result;
    }
}
