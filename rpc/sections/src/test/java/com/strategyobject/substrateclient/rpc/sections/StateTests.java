package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.codegen.RpcInterfaceInitializationException;
import com.strategyobject.substrateclient.rpc.codegen.RpcGeneratedSectionFactory;
import com.strategyobject.substrateclient.rpc.core.ParameterConverter;
import com.strategyobject.substrateclient.rpc.core.ResultConverter;
import com.strategyobject.substrateclient.rpc.types.Metadata;
import com.strategyobject.substrateclient.rpc.types.RuntimeVersion;
import com.strategyobject.substrateclient.tests.containers.SubstrateVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ws.WsProvider;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Testcontainers
public class StateTests {
    private static final int WAIT_TIMEOUT = 10;
    private static final Network network = Network.newNetwork();

    @Container
    static final TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0)
            .withNetwork(network);

    @Test
    void getRuntimeVersion() throws ExecutionException, InterruptedException, TimeoutException, RpcInterfaceInitializationException {
        try (WsProvider wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {
            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            val sectionFactory = new RpcGeneratedSectionFactory();

            // TO DO use real converter
            ParameterConverter parameterConverter = mock(ParameterConverter.class);
            when(parameterConverter.convert(any()))
                    .thenAnswer(invocation -> invocation);

            // TO DO use real converter
            ResultConverter resultConverter = mock(ResultConverter.class);
            when(resultConverter.convert(any()))
                    .thenAnswer(invocation -> new RuntimeVersion());

            State rpcSection = sectionFactory.create(State.class, wsProvider, parameterConverter, resultConverter);

            assertDoesNotThrow(() -> {
                rpcSection.getRuntimeVersion().get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            });
        }
    }

    @Test
    void getMetadata() throws ExecutionException, InterruptedException, TimeoutException, RpcInterfaceInitializationException {
        try (WsProvider wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {
            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            val sectionFactory = new RpcGeneratedSectionFactory();

            // TO DO use real converter
            ParameterConverter parameterConverter = mock(ParameterConverter.class);
            when(parameterConverter.convert(any()))
                    .thenAnswer(invocation -> invocation);

            // TO DO use real converter
            ResultConverter resultConverter = mock(ResultConverter.class);
            when(resultConverter.convert(any()))
                    .thenAnswer(invocation -> new Metadata());

            State rpcSection = sectionFactory.create(State.class, wsProvider, parameterConverter, resultConverter);

            assertDoesNotThrow(() -> {
                rpcSection.getMetadata().get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            });
        }
    }
}
