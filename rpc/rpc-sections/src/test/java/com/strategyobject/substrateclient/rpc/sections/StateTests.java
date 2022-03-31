package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.rpc.codegen.sections.RpcGeneratedSectionFactory;
import com.strategyobject.substrateclient.rpc.codegen.sections.RpcInterfaceInitializationException;
import com.strategyobject.substrateclient.rpc.types.StorageKey;
import com.strategyobject.substrateclient.tests.containers.SubstrateVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ws.WsProvider;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class StateTests {
    private static final int WAIT_TIMEOUT = 10;
    private static final Network network = Network.newNetwork();

    @Container
    static final TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0)
            .withNetwork(network);

    @Test
    void getRuntimeVersion() throws ExecutionException, InterruptedException, TimeoutException, RpcInterfaceInitializationException {
        try (WsProvider wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {
            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            val sectionFactory = new RpcGeneratedSectionFactory();
            State rpcSection = sectionFactory.create(State.class, wsProvider);

            assertDoesNotThrow(() -> {
                rpcSection.getRuntimeVersion().get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            });
        }
    }

    @Test
    void getMetadata() throws ExecutionException, InterruptedException, TimeoutException, RpcInterfaceInitializationException {
        try (WsProvider wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {
            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            val sectionFactory = new RpcGeneratedSectionFactory();
            State rpcSection = sectionFactory.create(State.class, wsProvider);

            assertDoesNotThrow(() -> {
                rpcSection.getMetadata().get(WAIT_TIMEOUT * 10, TimeUnit.SECONDS);
            });
        }
    }

    @Test
    void getKeys() throws ExecutionException, InterruptedException, TimeoutException, RpcInterfaceInitializationException {
        try (WsProvider wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {
            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            val sectionFactory = new RpcGeneratedSectionFactory();
            State rpcSection = sectionFactory.create(State.class, wsProvider);

            // xxhash128("Balances") = 0xc2261276cc9d1f8598ea4b6a74b15c2f
            // xxhash128("StorageVersion") = 0x308ce9615de0775a82f8a94dc3d285a1
            val key = "0xc2261276cc9d1f8598ea4b6a74b15c2f308ce9615de0775a82f8a94dc3d285a1"; // TODO implement and use `xxhash`
            val keys = rpcSection.getKeys(StorageKey.valueOf(HexConverter.toBytes(key))).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertTrue(keys.size() > 0);
        }
    }

    @Test
    void getStorage() throws ExecutionException, InterruptedException, TimeoutException, RpcInterfaceInitializationException {
        try (WsProvider wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {
            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            val sectionFactory = new RpcGeneratedSectionFactory();
            State rpcSection = sectionFactory.create(State.class, wsProvider);

            // xxhash128("Balances") = 0xc2261276cc9d1f8598ea4b6a74b15c2f
            // xxhash128("StorageVersion") = 0x308ce9615de0775a82f8a94dc3d285a1
            val key = "0xc2261276cc9d1f8598ea4b6a74b15c2f308ce9615de0775a82f8a94dc3d285a1"; // TODO implement and use `xxhash`
            val storageData = rpcSection.getStorage(StorageKey.valueOf(HexConverter.toBytes(key))).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertNotNull(storageData);
            assertTrue(storageData.getData().length > 0);
        }
    }

    @Test
    void getStorageHandlesNullResponse() throws ExecutionException, InterruptedException, TimeoutException, RpcInterfaceInitializationException {
        try (WsProvider wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {
            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            val sectionFactory = new RpcGeneratedSectionFactory();
            State rpcSection = sectionFactory.create(State.class, wsProvider);

            val emptyKey = new byte[32];
            val storageData = rpcSection.getStorage(StorageKey.valueOf(emptyKey)).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertNull(storageData);
        }
    }

    @Test
    void getStorageAt() throws ExecutionException, InterruptedException, TimeoutException, RpcInterfaceInitializationException {
        try (WsProvider wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {
            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            val sectionFactory = new RpcGeneratedSectionFactory();
            State rpcSection = sectionFactory.create(State.class, wsProvider);

            // xxhash128("Balances") = 0xc2261276cc9d1f8598ea4b6a74b15c2f
            // xxhash128("StorageVersion") = 0x308ce9615de0775a82f8a94dc3d285a1
            val key = "0xc2261276cc9d1f8598ea4b6a74b15c2f308ce9615de0775a82f8a94dc3d285a1"; // TODO implement and use `xxhash`
            val changes = rpcSection.queryStorageAt(Collections.singletonList(StorageKey.valueOf(HexConverter.toBytes(key)))).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertTrue(changes.size() > 0);
            assertTrue(changes.get(0).getChanges().size() > 0);
            assertNotNull(changes.get(0).getChanges().get(0).getValue0().getData());
            assertTrue(changes.get(0).getChanges().get(0).getValue0().getData().length > 0);
        }
    }
}
