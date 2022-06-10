package com.strategyobject.substrateclient.pallet.storage;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.crypto.Hasher;
import com.strategyobject.substrateclient.rpc.api.StorageKey;
import lombok.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Operates with StorageKey for a certain pallet and a storage.
 */
public class StorageKeyProvider {
    private static final int XX_HASH_SIZE = 16;
    @Getter(AccessLevel.PACKAGE)
    private final List<KeyHasher<?>> keyHashers = new ArrayList<>();
    private final ByteBuffer keyPrefix;

    private StorageKeyProvider(String palletName, String storageName) {
        keyPrefix = ByteBuffer.allocate(XX_HASH_SIZE * 2);

        xxHash128(keyPrefix, palletName);
        xxHash128(keyPrefix, storageName);
    }

    private static void xxHash128(ByteBuffer buf, String value) {
        val encodedValue = value.getBytes(StandardCharsets.UTF_8);

        final ByteOrder sourceOrder = buf.order(); // final ByteOrder instead of val because of checkstyle
        buf.order(ByteOrder.LITTLE_ENDIAN);

        buf.asLongBuffer()
                .put(Hasher.xx(0, encodedValue))
                .put(Hasher.xx(1, encodedValue));

        buf.position(buf.position() + XX_HASH_SIZE);
        buf.order(sourceOrder);
    }

    public static StorageKeyProvider of(@NonNull String palletName, @NonNull String storageName) {
        return new StorageKeyProvider(palletName, storageName);
    }

    public StorageKeyProvider use(@NonNull KeyHasher<?>... hashers) {
        Preconditions.checkState(keyHashers.isEmpty(),
                "Key hashers are already set.");

        Collections.addAll(keyHashers, hashers);

        return this;
    }

    /**
     * @param keys values of the keys for request to the entry
     *             Note: all the keys must follow the same exact order as
     *             they appear in the storage.
     * @return combined StorageKey that contains concatenated pallet name, storage name
     * and hashes of keys encoded by certain algorithms.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public StorageKey get(@NonNull Object... keys) {
        if (keys.length > keyHashers.size()) {
            throw new IndexOutOfBoundsException(
                    String.format("Number of keys mustn't exceed capacity. Passed: %s, capacity: %s.",
                            keys.length,
                            keyHashers.size()));
        }

        val stream = new ByteArrayOutputStream();
        try {
            stream.write(keyPrefix.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (var i = 0; i < keys.length; i++) {
            try {
                stream.write(((KeyHasher) keyHashers.get(i)).getHash(keys[i]));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return StorageKey.valueOf(stream.toByteArray());
    }

    /**
     * @return number of all keys of the storage.
     */
    public int countOfKeys() {
        return keyHashers.size();
    }

    /**
     * @param fullKey StorageKey that contains pallet and storage names and all keys
     *                of the storage's entry.
     * @return all keys of the entry.
     */
    public List<Object> extractKeys(StorageKey fullKey) {
        val stream = new ByteArrayInputStream(Arrays.copyOfRange(fullKey.getData(),
                keyPrefix.capacity(),
                fullKey.getData().length));

        return keyHashers
                .stream()
                .map(keyHasher -> {
                    try {
                        return keyHasher.extractKey(stream);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * @param fullKey            StorageKey that contains pallet and storage names and all keys
     *                           of the entry.
     * @param queryKey           StorageKey that contains pallet and storage names
     *                           and may contain some keys of the entry.
     *                           Note: all the keys must follow the same exact order as
     *                           their sequence in the storage.
     * @param countOfKeysInQuery number of keys included in the queryKey.
     * @return the remaining keys of the entry which are not included in the queryKey.
     */
    public List<Object> extractKeys(StorageKey fullKey, StorageKey queryKey, int countOfKeysInQuery) {
        val stream = new ByteArrayInputStream(Arrays.copyOfRange(fullKey.getData(),
                queryKey.getData().length,
                fullKey.getData().length));

        return keyHashers
                .stream()
                .skip(countOfKeysInQuery)
                .map(keyHasher -> {
                    try {
                        return keyHasher.extractKey(stream);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

}
