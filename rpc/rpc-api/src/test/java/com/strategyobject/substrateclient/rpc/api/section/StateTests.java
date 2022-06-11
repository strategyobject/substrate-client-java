package com.strategyobject.substrateclient.rpc.api.section;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.rpc.RpcGeneratedSectionFactory;
import com.strategyobject.substrateclient.rpc.api.BlockNumber;
import com.strategyobject.substrateclient.rpc.api.StorageKey;
import com.strategyobject.substrateclient.tests.containers.SubstrateVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ws.WsProvider;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class StateTests {
    private static final int WAIT_TIMEOUT = 10;
    private static final Network network = Network.newNetwork();

    private final StorageKey storageKey;

    @Container
    static final TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0)
            .withNetwork(network);

    StateTests() {
        // xxhash128("Balances") = 0xc2261276cc9d1f8598ea4b6a74b15c2f
        // xxhash128("StorageVersion") = 0x308ce9615de0775a82f8a94dc3d285a1
        val key = "0xc2261276cc9d1f8598ea4b6a74b15c2f308ce9615de0775a82f8a94dc3d285a1";
        storageKey = StorageKey.valueOf(HexConverter.toBytes(key));
    }

    @Test
    void getRuntimeVersion() throws Exception {
        try (val wsProvider = connect()) {
            val state = RpcGeneratedSectionFactory.create(State.class, wsProvider);

            assertDoesNotThrow(() -> {
                state.getRuntimeVersion().get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            });
        }
    }

    @Test
    void getMetadata() throws Exception {
        try (val wsProvider = connect()) {
            val state = RpcGeneratedSectionFactory.create(State.class, wsProvider);

            assertDoesNotThrow(() -> {
                state.getMetadata().get(WAIT_TIMEOUT * 10, TimeUnit.SECONDS);
            });
        }
    }

    @Test
    void getKeys() throws Exception {
        try (val wsProvider = connect()) {
            val state = RpcGeneratedSectionFactory.create(State.class, wsProvider);

            val keys = state.getKeys(storageKey).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertTrue(keys.size() > 0);
        }
    }

    @Test
    void getStorage() throws Exception {
        try (val wsProvider = connect()) {
            val state = RpcGeneratedSectionFactory.create(State.class, wsProvider);

            val storageData = state.getStorage(storageKey).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertNotNull(storageData);
            assertTrue(storageData.getData().length > 0);
        }
    }

    @Test
    void getStorageHandlesNullResponse() throws Exception {
        try (val wsProvider = connect()) {
            val state = RpcGeneratedSectionFactory.create(State.class, wsProvider);

            val emptyKey = new byte[32];
            val storageData = state.getStorage(StorageKey.valueOf(emptyKey)).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertNull(storageData);
        }
    }

    @Test
    void getStorageAtBlock() throws Exception {
        try (val wsProvider = connect()) {
            val chainSection = RpcGeneratedSectionFactory.create(Chain.class, wsProvider);
            val blockHash = chainSection.getBlockHash(BlockNumber.GENESIS).get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            val state = RpcGeneratedSectionFactory.create(State.class, wsProvider);

            val storageData = state.getStorage(storageKey, blockHash).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertNotNull(storageData);
            assertTrue(storageData.getData().length > 0);
        }
    }

    @Test
    void getStorageHash() throws Exception {
        try (val wsProvider = connect()) {
            val state = RpcGeneratedSectionFactory.create(State.class, wsProvider);

            val hash = state.getStorageHash(storageKey).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertNotNull(hash);
            assertTrue(hash.getData().length > 0);
        }
    }

    @Test
    void getStorageHashAt() throws Exception {
        try (val wsProvider = connect()) {
            val chainSection = RpcGeneratedSectionFactory.create(Chain.class, wsProvider);
            val blockHash = chainSection.getBlockHash(BlockNumber.GENESIS).get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            val state = RpcGeneratedSectionFactory.create(State.class, wsProvider);

            val hash = state.getStorageHash(storageKey, blockHash).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertNotNull(hash);
            assertTrue(hash.getData().length > 0);
        }
    }

    @Test
    void getStorageSize() throws Exception {
        try (val wsProvider = connect()) {
            val state = RpcGeneratedSectionFactory.create(State.class, wsProvider);

            val size = state.getStorageSize(storageKey).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            Assertions.assertEquals(1, size);
        }
    }

    @Test
    void getStorageSizeAt() throws Exception {
        try (val wsProvider = connect()) {
            val chainSection = RpcGeneratedSectionFactory.create(Chain.class, wsProvider);
            val blockHash = chainSection.getBlockHash(BlockNumber.GENESIS).get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            val state = RpcGeneratedSectionFactory.create(State.class, wsProvider);

            val size = state.getStorageSize(storageKey, blockHash).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            Assertions.assertEquals(1, size);
        }
    }

    @Test
    void queryStorageAt() throws Exception {
        try (val wsProvider = connect()) {
            val state = RpcGeneratedSectionFactory.create(State.class, wsProvider);

            val changes = state
                    .queryStorageAt(Collections.singletonList(storageKey))
                    .get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertTrue(changes.size() > 0);
            assertTrue(changes.get(0).getChanges().size() > 0);
            assertNotNull(changes.get(0).getChanges().get(0).getValue0().getData());
            assertTrue(changes.get(0).getChanges().get(0).getValue0().getData().length > 0);
        }
    }

    private WsProvider connect() throws Exception {
        val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build();

        wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);
        return wsProvider;
    }
}
