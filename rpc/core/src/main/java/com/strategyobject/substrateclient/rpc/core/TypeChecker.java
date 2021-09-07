package com.strategyobject.substrateclient.rpc.core;

import com.google.common.reflect.TypeToken;
import lombok.val;

import java.util.HashSet;
import java.util.Set;

class TypeChecker {
    private static final Set<String> simpleTypes = new HashSet<>();

    static {
        simpleTypes.add(TypeToken.of(boolean.class).toString());
        simpleTypes.add(TypeToken.of(byte.class).toString());
        simpleTypes.add(TypeToken.of(short.class).toString());
        simpleTypes.add(TypeToken.of(int.class).toString());
        simpleTypes.add(TypeToken.of(long.class).toString());

        simpleTypes.add(TypeToken.of(Boolean.class).toString());
        simpleTypes.add(TypeToken.of(Byte.class).toString());
        simpleTypes.add(TypeToken.of(Short.class).toString());
        simpleTypes.add(TypeToken.of(Integer.class).toString());
        simpleTypes.add(TypeToken.of(Long.class).toString());

        simpleTypes.add(TypeToken.of(String.class).toString());
    }

    public static <T> boolean isConvertible(T obj) {
        val key = TypeToken.of(obj.getClass()).getType();

        return !simpleTypes.contains(key);
    }
}
