package com.strategyobject.substrateclient.pallet.storage;

import com.strategyobject.substrateclient.common.types.tuple.Pair;
import com.strategyobject.substrateclient.crypto.ss58.SS58Codec;
import com.strategyobject.substrateclient.pallet.TestsHelper;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.rpc.api.primitives.BlockHash;
import com.strategyobject.substrateclient.rpc.api.primitives.BlockNumber;
import com.strategyobject.substrateclient.rpc.api.section.Chain;
import com.strategyobject.substrateclient.rpc.api.section.State;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.tests.containers.SubstrateVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ProviderInterface;
import com.strategyobject.substrateclient.transport.ws.ReconnectionPolicy;
import com.strategyobject.substrateclient.transport.ws.WsProvider;
import lombok.NonNull;
import lombok.val;
import lombok.var;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class StorageNMapImplTests {
    private static final int CONNECTION_TIMEOUT = 1000;
    private static final int WAIT_TIMEOUT = 10;
    @Container
    private final TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0).waitingFor(Wait.forLogMessage(".*Running JSON-RPC WS server.*", 1));

    @SuppressWarnings("unchecked")
    private static StorageNMapImpl<BlockHash> newSystemBlockHashStorage(State state) {
        return StorageNMapImpl.with(
                state,
                (ScaleReader<BlockHash>) TestsHelper.SCALE_READER_REGISTRY.resolve(BlockHash.class),
                StorageKeyProvider.of("System", "BlockHash")
                        .use(KeyHasher.with((ScaleWriter<BlockNumber>) TestsHelper.SCALE_WRITER_REGISTRY.resolve(BlockNumber.class),
                                (ScaleReader<BlockNumber>) TestsHelper.SCALE_READER_REGISTRY.resolve(BlockNumber.class),
                                TwoX64Concat.getInstance())));
    }

    @NonNull
    private WsProvider getConnectedProvider() throws InterruptedException, ExecutionException, TimeoutException {
        val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .withPolicy(ReconnectionPolicy.MANUAL)
                .build();
        wsProvider.connect().get(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
        return wsProvider;
    }

    @Test
    void keys() throws Exception {
        try (val wsProvider = getConnectedProvider()) {
            val state = TestsHelper.createSectionFactory(wsProvider).create(State.class);
            val storage = newSystemBlockHashStorage(state);

            val collection = storage.keys().get();

            assertNotNull(collection);
            assertEquals(1, collection.size());

            val blockNumber = new AtomicReference<BlockNumber>(null);
            collection.iterator().forEachRemaining(q -> q.consume((o -> blockNumber.set((BlockNumber) o.get(0)))));

            assertEquals(BlockNumber.GENESIS, blockNumber.get());

            val blocks = collection.multi().execute().get();
            val list = new ArrayList<>();
            blocks.iterator().forEachRemaining(e -> e.consume((value, keys) -> list.add(value)));

            assertEquals(1, list.size());

            val block = (BlockHash) list.stream().findFirst().orElseThrow(RuntimeException::new);
            assertNotEquals(BigInteger.ZERO, new BigInteger(block.getBytes()));
        }
    }

    @Test
    void multiToDifferentStorages() throws Exception {
        try (val wsProvider = getConnectedProvider()) {
            val state = TestsHelper.createSectionFactory(wsProvider).create(State.class);
            val storageValue = StorageNMapImpl.with(
                    state,
                    TestsHelper.SCALE_READER_REGISTRY.resolve(AccountId.class),
                    StorageKeyProvider.of("Sudo", "Key"));
            val storageMap = newSystemBlockHashStorage(state);

            val getKey = storageValue.query();
            val getHash = storageMap.query(BlockNumber.GENESIS);

            val multi = getKey.join(getHash);
            val collection = multi.execute().get();

            val expectedKey = AccountId.fromBytes(
                    SS58Codec.decode(
                                    "5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY")
                            .getAddress());
            val expectedBlock = storageMap.get(BlockNumber.GENESIS).get();

            val list = new ArrayList<>(2);
            collection.iterator().forEachRemaining(e -> e.consume((value, keys) -> list.add(value)));

            assertEquals(expectedKey, list.get(0));
            assertEquals(expectedBlock, list.get(1));
        }
    }

    @Test
    void entries() throws Exception {
        try (val wsProvider = getConnectedProvider()) {
            val state = TestsHelper.createSectionFactory(wsProvider).create(State.class);
            val storage = newSystemBlockHashStorage(state);

            val collection = storage.entries().get();

            assertNotNull(collection);

            val blockNumber = new AtomicReference<BlockNumber>(null);
            val blockHash = new AtomicReference<BlockHash>(null);
            collection.iterator().forEachRemaining(e -> e.consume((value, keys) -> {
                blockHash.set(value);
                blockNumber.set((BlockNumber) keys.get(0));
            }));

            assertEquals(BlockNumber.GENESIS, blockNumber.get());
            assertNotEquals(BigInteger.ZERO, new BigInteger(blockHash.get().getBytes()));
        }
    }

    @Test
    void multi() throws Exception {
        try (val wsProvider = getConnectedProvider()) {
            val state = TestsHelper.createSectionFactory(wsProvider).create(State.class);
            val storage = newSystemBlockHashStorage(state);

            val collection = storage.multi(
                            Arg.of(BlockNumber.GENESIS),
                            Arg.of(BlockNumber.of(1)))
                    .get();
            assertNotNull(collection);

            val list = new ArrayList<Pair<BlockNumber, BlockHash>>();
            collection.iterator()
                    .forEachRemaining(e ->
                            e.consume((value, keys) ->
                                    list.add(Pair.of((BlockNumber) keys.get(0), value))));

            assertEquals(2, list.size());

            assertEquals(BlockNumber.GENESIS, list.get(0).getValue0());
            assertNotEquals(BigInteger.ZERO, new BigInteger(list.get(0).getValue1().getBytes()));

            assertEquals(BlockNumber.of(1), list.get(1).getValue0());
            assertNull(list.get(1).getValue1());
        }
    }

    @Test
    void keysPaged() throws Exception {
        try (val wsProvider = getConnectedProvider()) {
            val state = TestsHelper.createSectionFactory(wsProvider).create(State.class);
            waitForNewBlocks(wsProvider);

            val storage = newSystemBlockHashStorage(state);

            int pageSize = 2;
            val pages = storage.keysPaged(pageSize);

            assertNotNull(pages);

            var pageCount = 0;
            AtomicInteger total = new AtomicInteger();
            while (pages.moveNext().join()) {
                pages.current().iterator().forEachRemaining(queryableKey -> total.getAndIncrement());
                pageCount++;
                assertEquals(pageCount, pages.number());
            }

            assertTrue(pageCount > 1);
            assertEquals(pageCount, Math.ceil((double) total.get() / pageSize));
        }
    }

    @Test
    void entriesPaged() throws Exception {
        try (val wsProvider = getConnectedProvider()) {
            val state = TestsHelper.createSectionFactory(wsProvider).create(State.class);
            waitForNewBlocks(wsProvider);

            val storage = newSystemBlockHashStorage(state);

            int pageSize = 2;
            val pages = storage.entriesPaged(pageSize);

            assertNotNull(pages);

            var pageCount = 0;
            val pairs = new ArrayList<Pair<BlockNumber, BlockHash>>();
            while (pages.moveNext().join()) {
                pages.current()
                        .iterator()
                        .forEachRemaining(e ->
                                e.consume((value, keys) -> {
                                    val key = (BlockNumber) keys.get(0);
                                    assertNotEquals(BigInteger.ZERO, new BigInteger(value.getBytes()));

                                    pairs.add(Pair.of(key, value));
                                }));
                pageCount++;
                assertEquals(pageCount, pages.number());
            }

            assertEquals(pairs.size(), pairs.stream().map(Pair::getValue0).collect(Collectors.toSet()).size());

            assertTrue(pageCount > 1);
            assertEquals(pageCount, Math.ceil((double) pairs.size() / pageSize));
        }
    }

    @Test
    void subscribe() throws Exception {
        try (val wsProvider = getConnectedProvider()) {
            val state = TestsHelper.createSectionFactory(wsProvider).create(State.class);
            val blockNumber = BlockNumber.of(2);
            val storage = newSystemBlockHashStorage(state);
            val blockHash = new AtomicReference<BlockHash>();
            val value = new AtomicReference<BlockHash>();
            val argument = new AtomicReference<BlockNumber>();
            val unsubscribe = storage.subscribe((exception, block, v, keys) -> {
                        if (exception == null) {
                            blockHash.set(block);
                            value.set(v);
                            argument.set((BlockNumber) keys.get(0));
                        }
                    }, Arg.of(blockNumber))
                    .get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            waitForNewBlocks(wsProvider);

            val chain = TestsHelper.createSectionFactory(wsProvider).create(Chain.class);
            val expectedValue = chain.getBlockHash(blockNumber).join();
            val history = storage.history(blockNumber).join();
            val changedAt = history.stream()
                    .findFirst()
                    .orElseThrow(RuntimeException::new)
                    .getValue0();
            val isUnsubscribed = unsubscribe.get().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertTrue(isUnsubscribed);
            assertEquals(changedAt, blockHash.get());
            assertEquals(expectedValue, value.get());
            assertEquals(expectedValue, value.get());
            assertEquals(blockNumber, argument.get());
        }
    }

    private void waitForNewBlocks(ProviderInterface wsProvider) throws Exception {
        val chain = TestsHelper.createSectionFactory(wsProvider).create(Chain.class);

        val blockCount = new AtomicInteger(0);
        val unsubscribeFunc = chain
                .subscribeNewHeads((e, h) -> {
                    if (e == null) {
                        blockCount.incrementAndGet();
                    }
                })
                .get(WAIT_TIMEOUT, TimeUnit.SECONDS);

        await()
                .atMost(WAIT_TIMEOUT * 3, TimeUnit.SECONDS)
                .untilAtomic(blockCount, greaterThan(3));

        unsubscribeFunc.get().join();
    }
}
