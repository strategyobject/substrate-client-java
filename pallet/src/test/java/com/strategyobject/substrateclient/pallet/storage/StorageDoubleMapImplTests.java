package com.strategyobject.substrateclient.pallet.storage;

import com.strategyobject.substrateclient.crypto.ss58.SS58Codec;
import com.strategyobject.substrateclient.rpc.RpcGeneratedSectionFactory;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.rpc.api.section.State;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import com.strategyobject.substrateclient.tests.containers.SubstrateVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ws.WsProvider;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNull;

@Testcontainers
class StorageDoubleMapImplTests {
    @Container
    static final TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0);
    private static final int CONNECTION_TIMEOUT = 1000;

    @Test
    @SuppressWarnings("unchecked")
    void societyVotes() throws Exception {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {
            wsProvider.connect().get(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);

            val state = RpcGeneratedSectionFactory.create(State.class, wsProvider);
            val storage = StorageDoubleMapImpl.with(
                    state,
                    (ScaleReader<Void>) ScaleReaderRegistry.resolve(Void.class),
                    StorageKeyProvider.of("Society", "Votes")
                            .use(KeyHasher.with((ScaleWriter<AccountId>) ScaleWriterRegistry.resolve(AccountId.class),
                                            (ScaleReader<AccountId>) ScaleReaderRegistry.resolve(AccountId.class),
                                            TwoX64Concat.getInstance()),
                                    KeyHasher.with((ScaleWriter<AccountId>) ScaleWriterRegistry.resolve(AccountId.class),
                                            (ScaleReader<AccountId>) ScaleReaderRegistry.resolve(AccountId.class),
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
