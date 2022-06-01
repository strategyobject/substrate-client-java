package com.strategyobject.substrateclient.pallet;

import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.rpc.api.section.State;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(staticName = "with")
public class GeneratedPalletResolver implements PalletResolver {
    private static final String CLASS_NAME_TEMPLATE = "%sImpl";
    private final @NonNull State state;

    @Override
    public <T> T resolve(Class<T> interfaceClass) {
        if (interfaceClass.getDeclaredAnnotationsByType(Pallet.class).length == 0) {
            throw new IllegalArgumentException(
                    String.format("%s can't be constructed because it is not annotated with @%s.",
                            interfaceClass.getSimpleName(),
                            Pallet.class.getSimpleName()));
        }

        Class<?> implClazz;
        try {
            implClazz = Class.forName(String.format(CLASS_NAME_TEMPLATE, interfaceClass.getCanonicalName()));
            val ctor = implClazz.getConstructor(State.class);

            return interfaceClass.cast(ctor.newInstance(state));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
