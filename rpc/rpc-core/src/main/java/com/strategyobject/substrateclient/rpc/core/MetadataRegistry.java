package com.strategyobject.substrateclient.rpc.core;

import java.util.concurrent.atomic.AtomicReference;

public final class MetadataRegistry {
    private static final short DEFAULT_SS58_VERSION = 42;
    private static volatile MetadataRegistry instance;
    private final AtomicReference<Short> ss58AddressFormat = new AtomicReference<>(DEFAULT_SS58_VERSION); // TODO it should be read from Metadata

    private MetadataRegistry() {
    }

    public static MetadataRegistry getInstance() {
        if (instance == null) {
            synchronized (MetadataRegistry.class) {
                if (instance == null) {
                    instance = new MetadataRegistry();
                }
            }
        }

        return instance;
    }

    public short getSS58AddressFormat() {
        return ss58AddressFormat.get();
    }

    // TODO it's a workaround to make possible changing network format until metadata is processed
    public void setSS58AddressFormat(short value) {
        ss58AddressFormat.getAndSet(value);
    }
}
