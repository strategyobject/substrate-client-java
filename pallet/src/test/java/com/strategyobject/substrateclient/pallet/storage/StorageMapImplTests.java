package com.strategyobject.substrateclient.pallet.storage;

import com.strategyobject.substrateclient.pallet.TestsHelper;
import com.strategyobject.substrateclient.rpc.api.primitives.BlockHash;
import com.strategyobject.substrateclient.rpc.api.section.State;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.tests.containers.FrequencyVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ws.ReconnectionPolicy;
import com.strategyobject.substrateclient.transport.ws.WsProvider;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Testcontainers
class StorageMapImplTests {
    @Container
    private final TestSubstrateContainer substrate = new TestSubstrateContainer(FrequencyVersion.CURRENT_VERSION).waitingFor(Wait.forLogMessage(".*Running JSON-RPC server.*", 1));
    private static final int CONNECTION_TIMEOUT = 1000;

    @Test
    @SuppressWarnings("unchecked")
    void systemBlockHash() throws Exception {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .withPolicy(ReconnectionPolicy.MANUAL)
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
