package com.strategyobject.substrateclient.storage;

import com.strategyobject.substrateclient.rpc.Rpc;
import com.strategyobject.substrateclient.rpc.types.StorageKey;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @param <V> the type of the value.
 */
@RequiredArgsConstructor(staticName = "with")
public class KeyCollectionImpl<V> implements KeyCollection<V> {
    private final @NonNull Rpc rpc;
    private final @NonNull List<StorageKey> storageKeys;
    private final @NonNull ScaleReader<V> valueReader;
    private final @NonNull Function<StorageKey, List<Object>> keyExtractor;

    @Override
    public int size() {
        return storageKeys.size();
    }

    @Override
    public MultiQuery<V> multi() {
        return () -> rpc
                .state()
                .queryStorageAt(storageKeys)
                .thenApplyAsync(changeSets -> HomogeneousKeyValueCollection.with(
                        changeSets.stream()
                                .flatMap(set -> set.getChanges().stream())
                                .collect(Collectors.toList()),
                        valueReader,
                        keyExtractor
                ));
    }

    @Override
    public Iterator<QueryableKey<V>> iterator() {
        return new Iterator<QueryableKey<V>>() {
            private final Iterator<StorageKey> underlying = storageKeys.iterator();

            @Override
            public boolean hasNext() {
                return underlying.hasNext();
            }

            @Override
            public QueryableKey<V> next() {
                if (hasNext()) {
                    val key = underlying.next();

                    return QueryableKeyImpl.with(rpc, key, valueReader, keyExtractor);
                }

                throw new NoSuchElementException();
            }
        };
    }
}
