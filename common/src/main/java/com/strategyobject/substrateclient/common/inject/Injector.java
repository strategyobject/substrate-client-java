package com.strategyobject.substrateclient.common.inject;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Array;
import java.util.function.Function;

@SuppressWarnings("rawtypes")
@RequiredArgsConstructor
public class Injector<T, I extends Dependant, R> {
    private final @NonNull Injection<T> injection;
    private final @NonNull Class<I> clazz;
    private final @NonNull Function<T, I> resolver;
    private final @NonNull Function<I, R> processor;

    @SuppressWarnings("unchecked")
    private I traverse(Injection<T> current) {
        if (current.getInjections() == null) {
            return resolver.apply(current.getDependant());
        }

        I[] dependencies = current.getInjections()
                .stream()
                .map(this::traverse)
                .toArray(size -> (I[]) Array.newInstance(clazz, size));

        return (I) resolver
                .apply(current.getDependant())
                .inject(dependencies);
    }

    public R process() {
        return processor.apply(traverse(injection));
    }
}
