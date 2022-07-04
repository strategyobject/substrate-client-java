package com.strategyobject.substrateclient.common.reflection;

import lombok.val;

public final class ClassUtils {

    public static boolean hasDefaultConstructor(Class<?> clazz) {
        for (val constructor : clazz.getConstructors()) {
            if (constructor.getParameterCount() == 0) {
                return true;
            }
        }

        return false;
    }

    private ClassUtils() {
    }
}
