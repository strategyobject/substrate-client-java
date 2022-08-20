package com.strategyobject.substrateclient.common.inject;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class Injection<T> {
    private final @NonNull T dependant;

    private List<Injection<T>> injections;

    @SuppressWarnings("unchecked")
    public Injection<T> inject(@NonNull Object... injections) {
        Preconditions.checkArgument(injections.length > 0);

        this.injections = Arrays.stream(injections)
                .map(x -> x instanceof Injection ?
                        (Injection<T>) x :
                        Injection.of((T) x))
                .collect(Collectors.toList());

        return this;
    }

    public static <T> Injection<T> of(T dependant) {
        return new Injection<>(dependant);
    }
}
