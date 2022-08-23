package com.strategyobject.substrateclient.common.inject;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
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

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T, D extends Dependant> D traverse(@NonNull Injection<T> injection,
                                                      @NonNull Class<D> clazz,
                                                      @NonNull Function<T, D> resolver) {
        if (injection.getInjections() == null) {
            return resolver.apply(injection.getDependant());
        }

        D[] dependencies = injection.getInjections()
                .stream()
                .map(x -> traverse(x, clazz, resolver))
                .toArray(size -> (D[]) Array.newInstance(clazz, size));

        return (D) resolver
                .apply(injection.getDependant())
                .inject(dependencies);
    }
}
