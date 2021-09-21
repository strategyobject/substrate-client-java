package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.rpc.codegen.RpcInterfaceInitializationException;
import com.strategyobject.substrateclient.rpc.codegen.RpcGeneratedSectionFactory;
import com.strategyobject.substrateclient.rpc.core.ParameterConverter;
import com.strategyobject.substrateclient.rpc.core.ResultConverter;
import com.strategyobject.substrateclient.rpc.types.BlockHash;
import com.strategyobject.substrateclient.rpc.types.Header;
import com.strategyobject.substrateclient.tests.containers.SubstrateVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ws.WsProvider;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigInteger;
import java.util.AbstractMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

            // TO DO use real converter
            ParameterConverter parameterConverter = mock(ParameterConverter.class);
            when(parameterConverter.convert(anyString()))
                    .thenAnswer(invocation -> invocation);

            // TO DO use real converter
            ResultConverter resultConverter = mock(ResultConverter.class);
            when(resultConverter.<String, BlockHash>convert(anyString()))
                    .thenAnswer(invocation -> BlockHash.fromBytes(HexConverter.toBytes(invocation.getArgument(0))));

            Chain rpcSection = sectionFactory.create(Chain.class, wsProvider, parameterConverter, resultConverter);

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

            ParameterConverter parameterConverter = mock(ParameterConverter.class);
            when(parameterConverter.convert(anyString()))
                    .thenAnswer(invocation -> {
                        throw new RuntimeException("Mustn't be called.");
                    });

            // TO DO use real converter
            ResultConverter resultConverter = mock(ResultConverter.class);
            when(resultConverter.<AbstractMap<?, ?>, Header>convert(any()))
                    .thenAnswer(invocation -> new Header(
                            BlockHash.fromBytes(
                                    HexConverter.toBytes((String) ((AbstractMap<?, ?>) invocation.getArgument(0)).get("parentHash"))
                            )));

            Chain rpcSection = sectionFactory.create(Chain.class, wsProvider, parameterConverter, resultConverter);

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

            // TO DO use real converter
            ParameterConverter parameterConverter = mock(ParameterConverter.class);
            when(parameterConverter.convert(any()))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // TO DO use real converter
            ResultConverter resultConverter = mock(ResultConverter.class);
            when(resultConverter.<String, BlockHash>convert(anyString()))
                    .thenAnswer(invocation -> BlockHash.fromBytes(HexConverter.toBytes(invocation.getArgument(0))));

            Chain rpcSection = sectionFactory.create(Chain.class, wsProvider, parameterConverter, resultConverter);

            BlockHash result = rpcSection.getBlockHash(0).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertNotEquals(new BigInteger(result.getData()), BigInteger.ZERO);
        }
    }
}
