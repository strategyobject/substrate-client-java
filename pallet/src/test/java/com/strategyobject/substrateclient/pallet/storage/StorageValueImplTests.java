package com.strategyobject.substrateclient.pallet.storage;

import com.strategyobject.substrateclient.crypto.ss58.SS58Codec;
import com.strategyobject.substrateclient.rpc.RpcGeneratedSectionFactory;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.rpc.api.BlockNumber;
import com.strategyobject.substrateclient.rpc.api.section.Chain;
import com.strategyobject.substrateclient.rpc.api.section.State;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.tests.containers.SubstrateVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ws.WsProvider;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class StorageValueImplTests {
    @Container
    static final TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0);
    private static final int CONNECTION_TIMEOUT = 1000;

    @Test
    void sudoKey() throws Exception {
        val expected = AccountId.fromBytes(
                SS58Codec.decode(
                                "5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY")
                        .getAddress());

        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {
            wsProvider.connect().get(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);

            val state = RpcGeneratedSectionFactory.create(State.class, wsProvider);
            val storage = StorageValueImpl.with(
                    state,
                    ScaleReaderRegistry.resolve(AccountId.class),
                    StorageKeyProvider.of("Sudo", "Key"));

            val actual = storage.get().get();

            assertEquals(expected, actual);
        }
    }

    @Test
    void sudoKeyAtGenesis() throws Exception {
        val expected = AccountId.fromBytes(
                SS58Codec.decode(
                                "5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY")
                        .getAddress());

        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {
            wsProvider.connect().get(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
            val state = RpcGeneratedSectionFactory.create(State.class, wsProvider);
            val chain = RpcGeneratedSectionFactory.create(Chain.class, wsProvider);

            val blockHash = chain.getBlockHash(BlockNumber.GENESIS).get();
            val storage = StorageValueImpl.with(
                    state,
                    ScaleReaderRegistry.resolve(AccountId.class),
                    StorageKeyProvider.of("Sudo", "Key"));

            val actual = storage.at(blockHash).get();

            assertEquals(expected, actual);
        }
    }
}
