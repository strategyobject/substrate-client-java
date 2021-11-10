package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.codegen.sections.RpcGeneratedSectionFactory;
import com.strategyobject.substrateclient.rpc.codegen.sections.RpcInterfaceInitializationException;
import com.strategyobject.substrateclient.rpc.types.BlockHash;
import com.strategyobject.substrateclient.tests.containers.SubstrateVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ws.WsProvider;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class ChainTests {
    private static final int WAIT_TIMEOUT = 10;
    private static final Network network = Network.newNetwork();

    @Container
    static final TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0)
            .withNetwork(network);

    @Test
    void getFinalizedHead() throws ExecutionException, InterruptedException, TimeoutException, RpcInterfaceInitializationException {
        try (WsProvider wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {
            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            val sectionFactory = new RpcGeneratedSectionFactory();
            Chain rpcSection = sectionFactory.create(Chain.class, wsProvider);

            BlockHash result = rpcSection.getFinalizedHead().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertNotEquals(new BigInteger(result.getData()), BigInteger.ZERO);
        }
    }

    @Test
    void subscribeNewHeads() throws ExecutionException, InterruptedException, TimeoutException, RpcInterfaceInitializationException {
        try (WsProvider wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {
            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            val sectionFactory = new RpcGeneratedSectionFactory();
            Chain rpcSection = sectionFactory.create(Chain.class, wsProvider);

            val blockCount = new AtomicInteger(0);
            val blockHash = new AtomicReference<BlockHash>(null);

            val unsubscribeFunc = rpcSection
                    .subscribeNewHeads((e, h) -> {
                        if (blockCount.incrementAndGet() > 1) {
                            blockHash.compareAndSet(null, h.getParentHash());
                        }
                    })
                    .get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            await()
                    .atMost(WAIT_TIMEOUT * 2, TimeUnit.SECONDS)
                    .untilAtomic(blockCount, greaterThan(2));

            assertNotEquals(new BigInteger(blockHash.get().getData()), BigInteger.ZERO);

            val result = unsubscribeFunc.get().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertTrue(result);
        }
    }

    @Test
    void getBlockHash() throws ExecutionException, InterruptedException, TimeoutException, RpcInterfaceInitializationException {
        try (WsProvider wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {
            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            val sectionFactory = new RpcGeneratedSectionFactory();
            Chain rpcSection = sectionFactory.create(Chain.class, wsProvider);

            BlockHash result = rpcSection.getBlockHash(0).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertNotEquals(new BigInteger(result.getData()), BigInteger.ZERO);
        }
    }
}
