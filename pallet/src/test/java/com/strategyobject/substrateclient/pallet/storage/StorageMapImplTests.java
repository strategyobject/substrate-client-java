package com.strategyobject.substrateclient.pallet.storage;

import com.strategyobject.substrateclient.rpc.RpcGeneratedSectionFactory;
import com.strategyobject.substrateclient.rpc.api.BlockHash;
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

            val state = RpcGeneratedSectionFactory.create(State.class, wsProvider);
            val storage = StorageMapImpl.with(
                    state,
                    (ScaleReader<BlockHash>) ScaleReaderRegistry.getInstance().resolve(BlockHash.class),
                    StorageKeyProvider.of("System", "BlockHash")
                            .use(KeyHasher.with((ScaleWriter<Integer>) ScaleWriterRegistry.getInstance().resolve(Integer.class),
                                    (ScaleReader<Integer>) ScaleReaderRegistry.getInstance().resolve(Integer.class),
                                    TwoX64Concat.getInstance())));

            val actual = storage.get(0).get();

            assertNotEquals(BigInteger.ZERO, new BigInteger(actual.getData()));
        }
    }
}
