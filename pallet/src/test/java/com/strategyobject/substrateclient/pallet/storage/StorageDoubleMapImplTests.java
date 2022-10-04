package com.strategyobject.substrateclient.pallet.storage;

import com.strategyobject.substrateclient.crypto.ss58.SS58Codec;
import com.strategyobject.substrateclient.pallet.TestsHelper;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.rpc.api.section.State;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.ScaleWriter;
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

import static org.junit.jupiter.api.Assertions.assertNull;

@Testcontainers
class StorageDoubleMapImplTests {
    @Container
    private final TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0).waitingFor(Wait.forLogMessage(".*Running JSON-RPC WS server.*", 1));
    private static final int CONNECTION_TIMEOUT = 1000;

    @Test
    @SuppressWarnings("unchecked")
    void societyVotes() throws Exception {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .withPolicy(ReconnectionPolicy.MANUAL)
                .build()) {
            wsProvider.connect().get(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);

            val state = TestsHelper.createSectionFactory(wsProvider).create(State.class);
            val storage = StorageDoubleMapImpl.with(
                    state,
                    (ScaleReader<Void>) TestsHelper.SCALE_READER_REGISTRY.resolve(Void.class),
                    StorageKeyProvider.of("Society", "Votes")
                            .use(
                                    KeyHasher.with((ScaleWriter<AccountId>) TestsHelper.SCALE_WRITER_REGISTRY.resolve(AccountId.class),
                                            (ScaleReader<AccountId>) TestsHelper.SCALE_READER_REGISTRY.resolve(AccountId.class),
                                            TwoX64Concat.getInstance()),
                                    KeyHasher.with((ScaleWriter<AccountId>) TestsHelper.SCALE_WRITER_REGISTRY.resolve(AccountId.class),
                                            (ScaleReader<AccountId>) TestsHelper.SCALE_READER_REGISTRY.resolve(AccountId.class),
                                            TwoX64Concat.getInstance())));
            val alice = AccountId.fromBytes(
                    SS58Codec.decode(
                                    "5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY")
                            .getAddress());
            val bob = AccountId.fromBytes(
                    SS58Codec.decode(
                                    "5FHneW46xGXgs5mUiveU4sbTyGBzmstUspZC92UhjJM694ty")
                            .getAddress());

            val actual = storage.get(alice, bob).get();

            assertNull(actual);
        }
    }
}
