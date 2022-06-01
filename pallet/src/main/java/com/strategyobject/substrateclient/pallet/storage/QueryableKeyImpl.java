package com.strategyobject.substrateclient.pallet.storage;

import com.strategyobject.substrateclient.rpc.api.section.State;
import com.strategyobject.substrateclient.rpc.api.StorageKey;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @param <V> the type of the value.
 */
@RequiredArgsConstructor(staticName = "with")
class QueryableKeyImpl<V> implements QueryableKey<V> {
    private final State state;
    private final StorageKey key;
    private final ScaleReader<V> valueReader;
    private final Function<StorageKey, List<Object>> keyExtractor;

    @Override
    public CompletableFuture<V> execute() {
        return state
                .getStorage(key)
                .thenApplyAsync(d -> {
                    if (d == null) {
                        return null;
                    }

                    try {
                        return valueReader.read(new ByteArrayInputStream(d.getData()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public StorageKey getKey() {
        return key;
    }

    @Override
    public ScaleReader<V> getValueReader() {
        return valueReader;
    }

    @Override
    public void consume(@NonNull KeyConsumer consumer) {
        val keys = keyExtractor.apply(key);

        consumer.accept(keys);
    }

    @Override
    public MultiQuery<Object> join(QueryableKey<?>... others) {
        return () -> state
                .queryStorageAt(concatKeys(others))
                .thenApplyAsync(changeSets -> DiverseKeyValueCollection.with(
                        changeSets.stream()
                                .flatMap(set -> set.getChanges().stream())
                                .collect(Collectors.toList()),
                        concatReaders(others),
                        concatKeyExtractors(others)

                ));
    }

    @Override
    public List<Object> extractKeys(@NonNull StorageKey fullKey) {
        return keyExtractor.apply(fullKey);
    }

    private ArrayList<StorageKey> concatKeys(QueryableKey<?>[] others) {
        val list = new ArrayList<>(Collections.singletonList(getKey()));
        list.addAll(Arrays.stream(others).map(QueryableKey::getKey).collect(Collectors.toList()));

        return list;
    }

    @SuppressWarnings("unchecked")
    private List<ScaleReader<Object>> concatReaders(QueryableKey<?>[] others) {
        val list = new ArrayList<>(Collections.singletonList((ScaleReader<Object>) getValueReader()));
        list.addAll(Arrays.stream(others).map(q -> (ScaleReader<Object>) q.getValueReader()).collect(Collectors.toList()));

        return list;
    }

    private ArrayList<Function<StorageKey, List<Object>>> concatKeyExtractors(QueryableKey<?>[] others) {
        val list = new ArrayList<Function<StorageKey, List<Object>>>(Collections.singletonList(this::extractKeys));
        list.addAll(Arrays.stream(others).map(q -> (Function<StorageKey, List<Object>>) q::extractKeys).collect(Collectors.toList()));

        return list;
    }
}
