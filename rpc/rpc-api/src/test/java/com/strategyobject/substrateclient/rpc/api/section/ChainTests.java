package com.strategyobject.substrateclient.rpc.api.section;

import com.strategyobject.substrateclient.rpc.api.primitives.BlockHash;
import com.strategyobject.substrateclient.rpc.api.primitives.BlockNumber;
import com.strategyobject.substrateclient.tests.containers.SubstrateVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ws.ReconnectionPolicy;
import com.strategyobject.substrateclient.transport.ws.WsProvider;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
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
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class ChainTests {
    private static final int WAIT_TIMEOUT = 10;
    private static final Network network = Network.newNetwork();

    @Container
    static final TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0).waitingFor(Wait.forLogMessage(".*Running JSON-RPC WS server.*", 1))
            .withNetwork(network);

    @Test
    void getFinalizedHead() throws Exception {
        try (val wsProvider = connect()) {
            val chain = TestsHelper.createSectionFactory(wsProvider).create(Chain.class);
            val result = chain.getFinalizedHead().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertNotEquals(BigInteger.ZERO, new BigInteger(result.getBytes()));
        }
    }

    @Test
    void subscribeNewHeads() throws Exception {
        try (val wsProvider = connect()) {
            val chain = TestsHelper.createSectionFactory(wsProvider).create(Chain.class);

            val blockCount = new AtomicInteger(0);
            val blockHash = new AtomicReference<BlockHash>(null);

            val unsubscribeFunc = chain
                    .subscribeNewHeads((e, h) -> {
                        if (blockCount.incrementAndGet() > 1) {
                            blockHash.compareAndSet(null, h.getParentHash());
                        }
                    })
                    .get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            await()
                    .atMost(WAIT_TIMEOUT * 2, TimeUnit.SECONDS)
                    .untilAtomic(blockCount, greaterThan(2));

            assertNotEquals(BigInteger.ZERO, new BigInteger(blockHash.get().getBytes()));

            val result = unsubscribeFunc.get().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            Assertions.assertTrue(result);
        }
    }

    @Test
    void getBlockHash() throws Exception {
        try (val wsProvider = connect()) {
            val chain = TestsHelper.createSectionFactory(wsProvider).create(Chain.class);
            val result = chain.getBlockHash().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertNotEquals(BigInteger.ZERO, new BigInteger(result.getBytes()));
        }
    }

    @Test
    void getBlock() throws Exception {
        try (val wsProvider = connect()) {
            val chain = TestsHelper.createSectionFactory(wsProvider).create(Chain.class);

            val height = new AtomicInteger(0);
            chain.subscribeNewHeads((e, header) -> {
                if (header != null) {
                    height.set(header.getNumber().getValue().intValue());
                }
            });

            await()
                    .atMost(WAIT_TIMEOUT * 3, TimeUnit.SECONDS)
                    .untilAtomic(height, greaterThan(1));

            val number = height.get();
            val blockHash = chain.getBlockHash(BlockNumber.of(number)).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertNotEquals(BigInteger.ZERO, new BigInteger(blockHash.getBytes()));

            val block = chain.getBlock(blockHash).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertNotEquals(BigInteger.ZERO, new BigInteger(block.getBlock().getHeader().getParentHash().getBytes()));
            assertEquals(number, block.getBlock().getHeader().getNumber().getValue().intValue());
        }
    }

    @Test
    void getCurrentBlock() throws ExecutionException, InterruptedException, TimeoutException {
        try (val wsProvider = connect()) {
            val chain = TestsHelper.createSectionFactory(wsProvider).create(Chain.class);

            val height = new AtomicInteger(0);
            chain.subscribeNewHeads((e, header) -> {
                if (header != null) {
                    height.set(header.getNumber().getValue().intValue());
                }
            });

            await()
                    .atMost(WAIT_TIMEOUT * 3, TimeUnit.SECONDS)
                    .untilAtomic(height, greaterThan(2));

            val block = chain.getBlock().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertNotEquals(BigInteger.ZERO, new BigInteger(block.getBlock().getHeader().getParentHash().getBytes()));
            assertNotNull(block.getBlock().getHeader().getNumber());
        }
    }

    private WsProvider connect() throws ExecutionException, InterruptedException, TimeoutException {
        val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .withPolicy(ReconnectionPolicy.MANUAL)
                .build();

        wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);
        return wsProvider;
    }
}
