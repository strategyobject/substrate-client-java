package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.codegen.RpcInterfaceInitializationException;
import com.strategyobject.substrateclient.rpc.codegen.RpcGeneratedSectionFactory;
import com.strategyobject.substrateclient.rpc.core.ParameterConverter;
import com.strategyobject.substrateclient.rpc.core.ResultConverter;
import com.strategyobject.substrateclient.rpc.types.AccountId;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Testcontainers
public class SystemTests {
    private static final int WAIT_TIMEOUT = 10;
    private static final Network network = Network.newNetwork();

    @Container
    static final TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0)
            .withNetwork(network);

    @Test
    void accountNextIndex() throws ExecutionException, InterruptedException, TimeoutException, RpcInterfaceInitializationException {
        try (WsProvider wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {
            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            val sectionFactory = new RpcGeneratedSectionFactory();

            // TO DO use real converter
            ParameterConverter parameterConverter = mock(ParameterConverter.class);
            when(parameterConverter.convert(any()))
                    .thenAnswer(invocation -> "5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY");

            // TO DO use real converter
            ResultConverter resultConverter = mock(ResultConverter.class);
            when(resultConverter.<Double, Integer>convert(any()))
                    .thenAnswer(invocation -> ((Double) invocation.getArgument(0)).intValue());

            System rpcSection = sectionFactory.create(System.class, wsProvider, parameterConverter, resultConverter);

            val result = rpcSection.accountNextIndex(AccountId.fromBytes(new byte[]{
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32
            })).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertEquals(result, 0);
        }
    }
}
