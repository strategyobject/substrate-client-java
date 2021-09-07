package com.strategyobject.substrateclient.rpc.codegen;

import com.strategyobject.substrateclient.rpc.codegen.substitutes.TestSection;
import com.strategyobject.substrateclient.rpc.core.ParameterConverter;
import com.strategyobject.substrateclient.rpc.core.ResultConverter;
import com.strategyobject.substrateclient.transport.ProviderInterface;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RpcSectionFactoryTest {
    @Test
    void createsRpcSectionAndCallMethod() throws ExecutionException, InterruptedException, RpcInterfaceInitializationException {
        val factory = new RpcSectionFactory();

        val expected = true;
        val sendFuture = CompletableFuture.completedFuture(String.valueOf(expected));
        val provider = mock(ProviderInterface.class);
        when(provider.<String>send(anyString(), anyList()))
                .thenReturn(sendFuture);

        val parameterConverter = mock(ParameterConverter.class);
        when(parameterConverter.convert(anyString()))
                .thenReturn("a");

        val resultConverter = mock(ResultConverter.class);
        when(resultConverter.<String, Boolean>convert(anyString()))
                .thenAnswer(invocation -> Boolean.valueOf(invocation.getArgument(0)));

        val rpcSection = factory.create(TestSection.class, provider, parameterConverter, resultConverter);

        val actual = rpcSection.doNothing(anyString()).get();

        assertEquals(expected, actual);
    }
}
