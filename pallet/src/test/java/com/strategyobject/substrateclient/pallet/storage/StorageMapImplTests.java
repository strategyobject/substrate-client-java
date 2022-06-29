package com.strategyobject.substrateclient.pallet.storage;

import com.strategyobject.substrateclient.pallet.TestsHelper;
import com.strategyobject.substrateclient.rpc.api.BlockHash;
import com.strategyobject.substrateclient.rpc.api.section.State;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.tests.containers.SubstrateVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ws.WsProvider;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Testcontainers
class StorageMapImplTests {
    @Container
    static final TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0);
    private static final int CONNECTION_TIMEOUT = 1000;

    @Test
    @SuppressWarnings("unchecked")
    void systemBlockHash() throws Exception {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {
            wsProvider.connect().get(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);

            val state = TestsHelper.createSectionFactory(wsProvider).create(State.class);
            val storage = StorageMapImpl.with(
                    state,
                    (ScaleReader<BlockHash>) TestsHelper.SCALE_READER_REGISTRY.resolve(BlockHash.class),
                    StorageKeyProvider.of("System", "BlockHash")
                            .use(KeyHasher.with((ScaleWriter<Integer>) TestsHelper.SCALE_WRITER_REGISTRY.resolve(Integer.class),
                                    (ScaleReader<Integer>) TestsHelper.SCALE_READER_REGISTRY.resolve(Integer.class),
                                    TwoX64Concat.getInstance())));

            val actual = storage.get(0).get();

            assertNotEquals(BigInteger.ZERO, new BigInteger(actual.getBytes()));
        }
    }
}
