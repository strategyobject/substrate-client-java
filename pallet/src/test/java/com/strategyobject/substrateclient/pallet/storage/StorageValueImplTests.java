package com.strategyobject.substrateclient.pallet.storage;

import com.strategyobject.substrateclient.crypto.ss58.SS58Codec;
import com.strategyobject.substrateclient.pallet.TestsHelper;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.rpc.api.primitives.BlockNumber;
import com.strategyobject.substrateclient.rpc.api.section.Chain;
import com.strategyobject.substrateclient.rpc.api.section.State;
import com.strategyobject.substrateclient.tests.containers.SubstrateVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ws.ReconnectionPolicy;
import com.strategyobject.substrateclient.transport.ws.WsProvider;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class StorageValueImplTests {
    @Container
    private final TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0).waitingFor(Wait.forLogMessage(".*Running JSON-RPC WS server.*", 1));
    private static final int CONNECTION_TIMEOUT = 1000;

    @Test
    void sudoKey() throws Exception {
        val expected = AccountId.fromBytes(
                SS58Codec.decode(
                                "5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY")
                        .getAddress());

        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .withPolicy(ReconnectionPolicy.MANUAL)
                .build()) {
            wsProvider.connect().get(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);

            val state = TestsHelper.createSectionFactory(wsProvider).create(State.class);
            val storage = StorageValueImpl.with(
                    state,
                    TestsHelper.SCALE_READER_REGISTRY.resolve(AccountId.class),
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
                .withPolicy(ReconnectionPolicy.MANUAL)
                .build()) {
            wsProvider.connect().get(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
            val state = TestsHelper.createSectionFactory(wsProvider).create(State.class);
            val chain = TestsHelper.createSectionFactory(wsProvider).create(Chain.class);

            val blockHash = chain.getBlockHash(BlockNumber.GENESIS).get();
            val storage = StorageValueImpl.with(
                    state,
                    TestsHelper.SCALE_READER_REGISTRY.resolve(AccountId.class),
                    StorageKeyProvider.of("Sudo", "Key"));

            val actual = storage.at(blockHash).get();

            assertEquals(expected, actual);
        }
    }
}
