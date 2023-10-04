package com.strategyobject.substrateclient.rpc.api.section;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import com.strategyobject.substrateclient.crypto.KeyPair;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.rpc.api.AccountIdScaleWrapper;
import com.strategyobject.substrateclient.rpc.api.AddressId;
import com.strategyobject.substrateclient.rpc.api.primitives.BlockNumber;
import com.strategyobject.substrateclient.rpc.api.primitives.Index;
import com.strategyobject.substrateclient.rpc.api.primitives.IndexU32;
import com.strategyobject.substrateclient.rpc.api.storage.StorageKey;
import com.strategyobject.substrateclient.tests.containers.FrequencyVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ws.ReconnectionPolicy;
import com.strategyobject.substrateclient.transport.ws.WsProvider;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigInteger;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class StateTests {
    private static final int WAIT_TIMEOUT = 10;
    private static final Network network = Network.newNetwork();

    private final StorageKey storageKey;

    @Container
    static final TestSubstrateContainer substrate = new TestSubstrateContainer(FrequencyVersion.CURRENT_VERSION).waitingFor(Wait.forLogMessage(".*Running JSON-RPC server.*", 1))
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
            val state = TestsHelper.createSectionFactory(wsProvider).create(State.class);

            assertDoesNotThrow(() -> {
                state.getRuntimeVersion().get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            });
        }
    }

    @Test
    void getMetadata() throws Exception {
        try (val wsProvider = connect()) {
            val state = TestsHelper.createSectionFactory(wsProvider).create(State.class);

            assertDoesNotThrow(() -> {
                state.getMetadata().get(WAIT_TIMEOUT * 10, TimeUnit.SECONDS);
            });
        }
    }

    @Test
    @Disabled("the storageKey is made from StorageVersion which was removed in Frequency 1.2.0")
    void getKeys() throws Exception {
        try (val wsProvider = connect()) {
            val state = TestsHelper.createSectionFactory(wsProvider).create(State.class);

            val keys = state.getKeys(storageKey).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertTrue(keys.size() > 0);
        }
    }

    @Test
    @Disabled("the storageKey is made from StorageVersion which was removed in Frequency 1.2.0")
    void getStorage() throws Exception {
        try (val wsProvider = connect()) {
            val state = TestsHelper.createSectionFactory(wsProvider).create(State.class);

            val storageData = state.getStorage(storageKey).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertNotNull(storageData);
            assertTrue(storageData.getData().length > 0);
        }
    }

    @Test
    void getStorageHandlesNullResponse() throws Exception {
        try (val wsProvider = connect()) {
            val state = TestsHelper.createSectionFactory(wsProvider).create(State.class);

            val emptyKey = new byte[32];
            val storageData = state.getStorage(StorageKey.valueOf(emptyKey)).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertNull(storageData);
        }
    }

    @Test
    @Disabled("the storageKey is made from StorageVersion which was removed in Frequency 1.2.0")
    void getStorageAtBlock() throws Exception {
        try (val wsProvider = connect()) {
            val chainSection = TestsHelper.createSectionFactory(wsProvider).create(Chain.class);
            val blockHash = chainSection.getBlockHash(BlockNumber.GENESIS).get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            val state = TestsHelper.createSectionFactory(wsProvider).create(State.class);

            val storageData = state.getStorage(storageKey, blockHash).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertNotNull(storageData);
            assertTrue(storageData.getData().length > 0);
        }
    }

    @Test
    @Disabled("the storageKey is made from StorageVersion which was removed in Frequency 1.2.0")
    void getStorageHash() throws Exception {
        try (val wsProvider = connect()) {
            val state = TestsHelper.createSectionFactory(wsProvider).create(State.class);

            val hash = state.getStorageHash(storageKey).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertNotNull(hash);
            assertTrue(hash.getBytes().length > 0);
        }
    }

    @Test
    @Disabled("the storageKey is made from StorageVersion which was removed in Frequency 1.2.0")
    void getStorageHashAt() throws Exception {
        try (val wsProvider = connect()) {
            val chainSection = TestsHelper.createSectionFactory(wsProvider).create(Chain.class);
            val blockHash = chainSection.getBlockHash(BlockNumber.GENESIS).get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            val state = TestsHelper.createSectionFactory(wsProvider).create(State.class);

            val hash = state.getStorageHash(storageKey, blockHash).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertNotNull(hash);
            assertTrue(hash.getBytes().length > 0);
        }
    }

    @Test
    @Disabled("the storageKey is made from StorageVersion which was removed in Frequency 1.2.0")
    void getStorageSize() throws Exception {
        try (val wsProvider = connect()) {
            val state = TestsHelper.createSectionFactory(wsProvider).create(State.class);

            val size = state.getStorageSize(storageKey).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            Assertions.assertEquals(1, size);
        }
    }

    @Test
    @Disabled("the storageKey is made from StorageVersion which was removed in Frequency 1.2.0")
    void getStorageSizeAt() throws Exception {
        try (val wsProvider = connect()) {
            val chainSection = TestsHelper.createSectionFactory(wsProvider).create(Chain.class);
            val blockHash = chainSection.getBlockHash(BlockNumber.GENESIS).get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            val state = TestsHelper.createSectionFactory(wsProvider).create(State.class);

            val size = state.getStorageSize(storageKey, blockHash).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            Assertions.assertEquals(1, size);
        }
    }

    @Test
    void queryStorageAt() throws Exception {
        try (val wsProvider = connect()) {
            val state = TestsHelper.createSectionFactory(wsProvider).create(State.class);

            val changes = state
                    .queryStorageAt(Collections.singletonList(storageKey))
                    .get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertTrue(changes.size() > 0);
            assertTrue(changes.get(0).getChanges().size() > 0);
            assertNotNull(changes.get(0).getChanges().get(0).getValue0().getData());
            assertTrue(changes.get(0).getChanges().get(0).getValue0().getData().length > 0);
        }
    }

    @Test
    public void retrieveAccountNonce() throws Exception {
        try (val wsProvider = connect()) {
            val state = TestsHelper.createSectionFactory(wsProvider).create(State.class);
            val aliceAddress = AccountId.fromBytes(aliceKeyPair().asPublicKey().getBytes());
            AddressId.fromBytes(aliceAddress.getBytes());
            CompletableFuture<IndexU32> nonceFuture = state.retrieveAccountNonce("AccountNonceApi_account_nonce", AccountIdScaleWrapper.fromBytes(aliceAddress.getBytes()));
            IndexU32 index = nonceFuture.get();

            Assertions.assertEquals(Long.valueOf(0L), index.getValue());
        }
    }

    private WsProvider connect() throws Exception {
        val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .withPolicy(ReconnectionPolicy.MANUAL)
                .build();

        wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);
        return wsProvider;
    }

    private KeyPair aliceKeyPair() {
        val str = "0x98319d4ff8a9508c4bb0cf0b5a78d760a0b2082c02775e6e82370816fedfff48925a225d97aa00682d6a59b95b18780c10d" +
            "7032336e88f3442b42361f4a66011d43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d";

        return KeyPair.fromBytes(HexConverter.toBytes(str));
    }
}
