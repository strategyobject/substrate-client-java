package com.strategyobject.substrateclient.scale;

import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.val;

public final class ScaleRegistryHelper {
    private ScaleRegistryHelper() {
    }

    @SuppressWarnings("unchecked")
    public static <T> ScaleReader<T> resolveAndInjectOrNull(Class<T> clazz, ScaleReader<?>... dependencies) {
        val target = (ScaleReader<T>) ScaleReaderRegistry.resolve(clazz);

        if (target == null) {
            return null;
        }

        return target.inject(dependencies);
    }

    @SuppressWarnings("unchecked")
    public static <T> ScaleWriter<T> resolveAndInjectOrNull(Class<T> clazz, ScaleWriter<?>... dependencies) {
        val target = (ScaleWriter<T>) ScaleWriterRegistry.resolve(clazz);

        if (target == null) {
            return null;
        }

        return target.inject(dependencies);
    }
}
