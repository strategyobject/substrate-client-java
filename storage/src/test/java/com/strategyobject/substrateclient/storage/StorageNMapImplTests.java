package com.strategyobject.substrateclient.storage;

import com.strategyobject.substrateclient.crypto.ss58.SS58Codec;
import com.strategyobject.substrateclient.rpc.RpcImpl;
import com.strategyobject.substrateclient.rpc.types.AccountId;
import com.strategyobject.substrateclient.rpc.types.BlockHash;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import com.strategyobject.substrateclient.tests.containers.SubstrateVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ws.WsProvider;
import com.strategyobject.substrateclient.types.tuples.Pair;
import lombok.NonNull;
import lombok.val;
import lombok.var;
import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;
import static org.testcontainers.shaded.org.hamcrest.number.OrderingComparison.greaterThan;

@Testcontainers
public class StorageNMapImplTests {
    private static final int CONNECTION_TIMEOUT = 1000;
    private static final int WAIT_TIMEOUT = 10;
    @Container
    private final TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0);

    @SuppressWarnings("unchecked")
    private static StorageNMapImpl<BlockHash> newSystemBlockHashStorage(RpcImpl rpc) {
        return StorageNMapImpl.with(
                rpc,
                (ScaleReader<BlockHash>) ScaleReaderRegistry.getInstance().resolve(BlockHash.class),
                StorageKeyProvider.of("System", "BlockHash")
                        .use(KeyHasher.with((ScaleWriter<Integer>) ScaleWriterRegistry.getInstance().resolve(Integer.class),
                                (ScaleReader<Integer>) ScaleReaderRegistry.getInstance().resolve(Integer.class),
                                TwoX64Concat.getInstance())));
    }

    @NonNull
    private WsProvider getConnectedProvider() throws InterruptedException, ExecutionException, TimeoutException {
        val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build();
        wsProvider.connect().get(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
        return wsProvider;
    }

    @Test
    public void keys() throws Exception {
        val wsProvider = getConnectedProvider();
        try (val rpc = RpcImpl.with(wsProvider)) {
            val storage = newSystemBlockHashStorage(rpc);

            val collection = storage.keys().get();

            assertNotNull(collection);
            assertEquals(1, collection.size());

            val blockNumber = new AtomicReference<Integer>(null);
            collection.iterator().forEachRemaining(q -> q.consume((o -> blockNumber.set((Integer) o.get(0)))));

            assertEquals(0, blockNumber.get());

            val blocks = collection.multi().execute().get();
            val list = new ArrayList<>();
            blocks.iterator().forEachRemaining(e -> e.consume((value, keys) -> list.add(value)));

            assertEquals(1, list.size());

            val block = (BlockHash) list.stream().findFirst().orElseThrow(RuntimeException::new);
            assertNotEquals(BigInteger.ZERO, new BigInteger(block.getData()));
        }
    }

    @Test
    public void multiToDifferentStorages() throws Exception {
        val wsProvider = getConnectedProvider();
        try (val rpc = RpcImpl.with(wsProvider)) {
            val storageValue = StorageNMapImpl.with(
                    rpc,
                    ScaleReaderRegistry.getInstance().resolve(AccountId.class),
                    StorageKeyProvider.of("Sudo", "Key"));
            val storageMap = newSystemBlockHashStorage(rpc);

            val getKey = storageValue.query();
            val getHash = storageMap.query(0);

            val multi = getKey.join(getHash);
            val collection = multi.execute().get();

            val expectedKey = AccountId.fromBytes(
                    SS58Codec.decode(
                                    "5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY")
                            .getAddress());
            val expectedBlock = storageMap.get(0).get();

            val list = new ArrayList<>(2);
            collection.iterator().forEachRemaining(e -> e.consume((value, keys) -> list.add(value)));

            assertEquals(expectedKey, list.get(0));
            assertEquals(expectedBlock, list.get(1));
        }
    }

    @Test
    public void entries() throws Exception {
        val wsProvider = getConnectedProvider();
        try (val rpc = RpcImpl.with(wsProvider)) {
            val storage = newSystemBlockHashStorage(rpc);

            val collection = storage.entries().get();

            assertNotNull(collection);

            val blockNumber = new AtomicReference<Integer>(null);
            val blockHash = new AtomicReference<BlockHash>(null);
            collection.iterator().forEachRemaining(e -> e.consume((value, keys) -> {
                blockHash.set(value);
                blockNumber.set((Integer) keys.get(0));
            }));

            assertEquals(0, blockNumber.get());
            assertNotEquals(BigInteger.ZERO, new BigInteger(blockHash.get().getData()));
        }
    }

    @Test
    public void multi() throws Exception {
        val wsProvider = getConnectedProvider();
        try (val rpc = RpcImpl.with(wsProvider)) {
            val storage = newSystemBlockHashStorage(rpc);

            val collection = storage.multi(Arg.of(0), Arg.of(1)).get();
            assertNotNull(collection);

            val list = new ArrayList<Pair<Integer, BlockHash>>();
            collection.iterator().forEachRemaining(e -> e.consume((value, keys) -> list.add(Pair.of((Integer) keys.get(0), value))));

            assertEquals(2, list.size());

            assertEquals(0, list.get(0).getValue0());
            assertNotEquals(BigInteger.ZERO, new BigInteger(list.get(0).getValue1().getData()));

            assertEquals(1, list.get(1).getValue0());
            assertNull(list.get(1).getValue1());
        }
    }

    @Test
    public void keysPaged() throws Exception {
        val wsProvider = getConnectedProvider();
        try (val rpc = RpcImpl.with(wsProvider)) {
            waitForNewBlocks(rpc);

            val storage = newSystemBlockHashStorage(rpc);

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
    public void entriesPaged() throws Exception {
        val wsProvider = getConnectedProvider();
        try (val rpc = RpcImpl.with(wsProvider)) {
            waitForNewBlocks(rpc);

            val storage = newSystemBlockHashStorage(rpc);

            int pageSize = 2;
            val pages = storage.entriesPaged(pageSize);

            assertNotNull(pages);

            var pageCount = 0;
            val pairs = new ArrayList<Pair<Integer, BlockHash>>();
            while (pages.moveNext().join()) {
                pages.current().iterator().forEachRemaining(e -> e.consume((value, keys) -> {
                    val key = (Integer) keys.get(0);
                    assertNotEquals(BigInteger.ZERO, new BigInteger(value.getData()));

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
    public void subscribe() throws Exception {
        val wsProvider = getConnectedProvider();
        try (val rpc = RpcImpl.with(wsProvider)) {
            val blockNumber = 2;
            val storage = newSystemBlockHashStorage(rpc);
            val blockHash = new AtomicReference<BlockHash>();
            val value = new AtomicReference<BlockHash>();
            val argument = new AtomicInteger();
            val unsubscribe = storage.subscribe((exception, block, v, keys) -> {
                        if (exception == null) {
                            blockHash.set(block);
                            value.set(v);
                            argument.set((Integer) keys.get(0));
                        }
                    }, Arg.of(blockNumber))
                    .get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            waitForNewBlocks(rpc);

            val expectedValue = rpc.chain().getBlockHash(blockNumber).join();
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

    private void waitForNewBlocks(RpcImpl rpc) throws InterruptedException, ExecutionException, TimeoutException {
        val blockCount = new AtomicInteger(0);
        val unsubscribeFunc = rpc
                .chain()
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
