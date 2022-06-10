package com.strategyobject.substrateclient.pallet.storage;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.types.tuple.Pair;
import com.strategyobject.substrateclient.rpc.api.StorageData;
import com.strategyobject.substrateclient.rpc.api.StorageKey;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.val;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * Collection that contains entries from various storage types.
 */
public class DiverseKeyValueCollection implements KeyValueCollection<Object> {
    private final List<ScaleReader<Object>> valueReaders;
    private final List<Pair<StorageKey, StorageData>> pairs;
    private final List<Function<StorageKey, List<Object>>> keyExtractors;

    private DiverseKeyValueCollection(List<Pair<StorageKey, StorageData>> pairs,
                                      List<ScaleReader<Object>> valueReaders,
                                      List<Function<StorageKey, List<Object>>> keyExtractors) {

        Preconditions.checkArgument(pairs.size() == valueReaders.size(),
                "Number of value readers doesn't match number of pairs.");

        Preconditions.checkArgument(pairs.size() == keyExtractors.size(),
                "Number of extractors doesn't match number of pairs.");

        this.valueReaders = valueReaders;
        this.pairs = pairs;
        this.keyExtractors = keyExtractors;
    }

    /**
     * @param pairs         list of pairs of storage's keys and data.
     * @param valueReaders  list of scale readers which read the value from StorageData
     *                      of the appropriate pair.
     * @param keyExtractors list of functions which extract keys
     *                      from StorageKey of the appropriate pair.
     * @return certain collection.
     */
    public static KeyValueCollection<Object> with(@NonNull List<Pair<StorageKey, StorageData>> pairs,
                                                  List<ScaleReader<Object>> valueReaders,
                                                  @NonNull List<Function<StorageKey, List<Object>>> keyExtractors) {
        return new DiverseKeyValueCollection(pairs, valueReaders, keyExtractors);
    }

    /**
     * @return iterator of entries.
     */
    @Override
    public Iterator<Entry<Object>> iterator() {
        return new Iterator<Entry<Object>>() {
            private final Iterator<Pair<StorageKey, StorageData>> underlying = pairs.iterator();
            private final Iterator<ScaleReader<Object>> readers = valueReaders.iterator();
            private final Iterator<Function<StorageKey, List<Object>>> extractors = keyExtractors.iterator();

            @Override
            public boolean hasNext() {
                return underlying.hasNext();
            }

            @Override
            public Entry<Object> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                val pair = underlying.next();

                return consumer -> {
                    try {
                        val nextReader = readers.next();
                        val value = pair.getValue1() == null ?
                                null :
                                nextReader.read(new ByteArrayInputStream(pair.getValue1().getData()));
                        val keys = extractors.next().apply(pair.getValue0());

                        consumer.accept(value, keys);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                };
            }
        };
    }
}
