package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.common.utils.Convert;
import com.strategyobject.substrateclient.rpc.codegen.RpcInterfaceInitializationException;
import com.strategyobject.substrateclient.rpc.codegen.RpcSectionFactory;
import com.strategyobject.substrateclient.rpc.core.ParameterConverter;
import com.strategyobject.substrateclient.rpc.core.ResultConverter;
import com.strategyobject.substrateclient.tests.containers.SubstrateVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ws.WsProvider;
import com.strategyobject.substrateclient.types.BlockHash;
import com.strategyobject.substrateclient.types.Header;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Testcontainers
public class ChainTests {
    private static final int WAIT_TIMEOUT = 10;
    private static final Network network = Network.newNetwork();

    @Container
    static TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0)
            .withNetwork(network);

    @Test
    void getFinalizedHead() throws ExecutionException, InterruptedException, TimeoutException, RpcInterfaceInitializationException {
        try (WsProvider wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {
            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            val sectionFactory = new RpcSectionFactory();

            ParameterConverter parameterConverter = mock(ParameterConverter.class);
            when(parameterConverter.convert(anyString()))
                    .thenAnswer(invocation -> invocation);

            // TO DO use real converter
            ResultConverter resultConverter = mock(ResultConverter.class);
            when(resultConverter.<String, BlockHash>convert(anyString()))
                    .thenAnswer(invocation -> new BlockHash(Convert.toBytes(invocation.getArgument(0))));

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

            val sectionFactory = new RpcSectionFactory();

            ParameterConverter parameterConverter = mock(ParameterConverter.class);
            when(parameterConverter.convert(anyString()))
                    .thenAnswer(invocation -> {
                        throw new RuntimeException("Mustn't be called.");
                    });

            // TO DO use real converter
            ResultConverter resultConverter = mock(ResultConverter.class);
            when(resultConverter.<AbstractMap<?, ?>, Header>convert(any()))
                    .thenAnswer(invocation -> new Header(
                            new BlockHash(
                                    Convert.toBytes((String) ((AbstractMap<?, ?>) invocation.getArgument(0)).get("parentHash"))
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
}
