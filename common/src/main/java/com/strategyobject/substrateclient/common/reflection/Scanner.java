package com.strategyobject.substrateclient.common.reflection;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.Set;

public final class Scanner {
    private static final Reflections reflections;

    static {
        reflections = new Reflections("", new SubTypesScanner());
    }

    public static <T> Set<Class<? extends T>> getSubTypesOf(Class<T> clazz) {
        return reflections.getSubTypesOf(clazz);
    }

    private Scanner() {
    }
}
