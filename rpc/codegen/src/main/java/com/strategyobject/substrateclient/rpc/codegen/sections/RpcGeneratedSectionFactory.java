package com.strategyobject.substrateclient.rpc.codegen.sections;

import com.strategyobject.substrateclient.rpc.core.annotations.RpcInterface;
import com.strategyobject.substrateclient.transport.ProviderInterface;
import lombok.NonNull;
import lombok.val;

import java.lang.reflect.InvocationTargetException;

import static com.strategyobject.substrateclient.rpc.codegen.sections.Constants.CLASS_NAME_TEMPLATE;

public class RpcGeneratedSectionFactory {
    public <T> T create(@NonNull Class<T> interfaceClass,
                        @NonNull ProviderInterface provider) throws RpcInterfaceInitializationException {
        if (interfaceClass.getDeclaredAnnotationsByType(RpcInterface.class).length == 0) {
            throw new IllegalArgumentException(
                    String.format("`%s` can't be constructed because isn't annotated with `@%s`.",
                            interfaceClass.getSimpleName(),
                            RpcInterface.class.getSimpleName()));
        }

        Class<?> clazz;
        try {
            clazz = Class.forName(String.format(CLASS_NAME_TEMPLATE, interfaceClass.getCanonicalName()));
            val ctor = clazz.getConstructor(ProviderInterface.class);

            return interfaceClass.cast(ctor.newInstance(provider));
        } catch (ClassCastException | ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RpcInterfaceInitializationException(e);
        }
    }
}
