package com.strategyobject.substrateclient.pallet;

import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.rpc.api.section.State;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class GeneratedPalletFactory implements PalletFactory {
    private static final String CLASS_NAME_TEMPLATE = "%sImpl";

    private final @NonNull ScaleReaderRegistry scaleReaderRegistry;
    private final @NonNull ScaleWriterRegistry scaleWriterRegistry;
    private final @NonNull State state;

    @Override
    public <T> T create(Class<T> interfaceClass) {
        if (interfaceClass.getDeclaredAnnotationsByType(Pallet.class).length == 0) {
            throw new IllegalArgumentException(
                    String.format("%s can't be constructed because it is not annotated with @%s.",
                            interfaceClass.getSimpleName(),
                            Pallet.class.getSimpleName()));
        }

        Class<?> implClazz;
        try {
            implClazz = Class.forName(String.format(CLASS_NAME_TEMPLATE, interfaceClass.getCanonicalName()));
            val ctor = implClazz.getConstructor(ScaleReaderRegistry.class, ScaleWriterRegistry.class, State.class);

            return interfaceClass.cast(ctor.newInstance(scaleReaderRegistry, scaleWriterRegistry, state));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
