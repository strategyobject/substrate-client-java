package com.strategyobject.substrateclient.pallet;

import com.strategyobject.substrateclient.pallet.annotations.Pallet;
import com.strategyobject.substrateclient.rpc.Rpc;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(staticName = "with")
public class GeneratedPalletResolver implements PalletResolver {
    private static final String CLASS_NAME_TEMPLATE = "%sImpl";
    private final @NonNull Rpc rpc;

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
            val ctor = implClazz.getConstructor(Rpc.class);

            return interfaceClass.cast(ctor.newInstance(rpc));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
