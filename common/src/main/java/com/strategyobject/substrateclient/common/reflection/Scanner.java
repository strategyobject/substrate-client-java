package com.strategyobject.substrateclient.common.reflection;

import lombok.NonNull;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public final class Scanner {
    private final Reflections reflections;

    private Scanner(String[] prefixes) {
        reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(
                                Arrays.stream(prefixes)
                                        .flatMap(p -> ClasspathHelper.forPackage(p).stream())
                                        .collect(Collectors.toCollection(ArrayList::new))));
    }

    public static Scanner forPrefixes(@NonNull String... prefixes) {
        return new Scanner(prefixes);
    }

    public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> clazz) {
        return reflections.getSubTypesOf(clazz);
    }

    public <T extends Annotation> Set<Class<?>> getTypesAnnotatedWith(Class<T> annotation) {
        return reflections.getTypesAnnotatedWith(annotation);
    }
}
