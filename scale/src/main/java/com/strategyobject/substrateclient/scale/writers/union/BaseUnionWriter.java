package com.strategyobject.substrateclient.scale.writers.union;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.types.union.Union;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.function.Function;

public abstract class BaseUnionWriter<T extends Union> implements ScaleWriter<T> {
    private final int unionSize;
    private final Function<T, ?>[] getItem;

    @SafeVarargs
    protected BaseUnionWriter(int unionSize, Function<T, ?>... getItem) {
        Preconditions.checkArgument(getItem.length == unionSize);

        this.unionSize = unionSize;
        this.getItem = getItem;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void write(@NonNull T value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers.length == unionSize);
        Arrays.stream(writers).forEach(Preconditions::checkNotNull);

        val index = value.getIndex();
        stream.write(index);
        ((ScaleWriter) writers[index]).write(getItem[index].apply(value), stream);
    }
}
