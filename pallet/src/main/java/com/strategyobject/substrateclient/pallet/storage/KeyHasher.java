package com.strategyobject.substrateclient.pallet.storage;

import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Represents entity which is responsible for hashing key (using a particular algorithm)
 * and extracting the one from hashed data.
 * @param <T> the type of the key.
 */
@RequiredArgsConstructor(staticName = "with")
public class KeyHasher<T> {
    private final @NonNull ScaleWriter<T> keyWriter;
    private final @NonNull ScaleReader<T> keyReader;
    private final @NonNull KeyHashingAlgorithm algorithm;

    /**
     * @param key one of the keys of the storage.
     * @return hash of the key.
     * @throws IOException when writing the key to scale fails.
     */
    public byte[] getHash(T key) throws IOException {
        val buf = new ByteArrayOutputStream();
        keyWriter.write(key, buf);

        return algorithm.getHash(buf.toByteArray());
    }

    /**
     * @param storageKeySuffix binary data that starts from the hashed key
     *                         and can contain the other hashed keys of the storage's entry.
     * @return key.
     * @throws IOException when reading the key from scale fails.
     */
    public T extractKey(@NonNull InputStream storageKeySuffix) throws IOException {
        val skip = storageKeySuffix.skip(algorithm.hashSize());
        if (skip != algorithm.hashSize()) {
            throw new RuntimeException("Stream couldn't skip the hash.");
        }

        return keyReader.read(storageKeySuffix);
    }
}
