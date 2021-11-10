package com.strategyobject.substrateclient.rpc.codegen.sections;

import com.strategyobject.substrateclient.rpc.codegen.substitutes.TestSection;
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
    // TODO move this test out of the project
    @Test
    void createsRpcSectionAndCallsMethod() throws ExecutionException, InterruptedException, RpcInterfaceInitializationException {
        val expected = true;
        val sendFuture = CompletableFuture.completedFuture((Object) Boolean.valueOf(expected));
        val provider = mock(ProviderInterface.class);
        when(provider.send(anyString(), anyList()))
                .thenReturn(sendFuture);

        val factory = new RpcGeneratedSectionFactory();
        val rpcSection = factory.create(TestSection.class, provider);

        val actual = rpcSection.doNothing("some").get();

        assertEquals(expected, actual);
    }
}
