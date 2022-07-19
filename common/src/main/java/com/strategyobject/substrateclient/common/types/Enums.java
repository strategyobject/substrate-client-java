package com.strategyobject.substrateclient.common.types;

import com.google.common.base.Preconditions;
import lombok.NonNull;

public class Enums {
    public static <E extends Enum<E>> E lookup(E @NonNull [] enumValues, int index) {
        Preconditions.checkArgument(enumValues.length > 0);
        Preconditions.checkArgument(index >= 0);
        Preconditions.checkArgument(index < enumValues.length,
                enumValues[0].getClass().getSimpleName() + " has no value associated with index " + index);

        return enumValues[index];
    }

    private Enums() {
    }
}
