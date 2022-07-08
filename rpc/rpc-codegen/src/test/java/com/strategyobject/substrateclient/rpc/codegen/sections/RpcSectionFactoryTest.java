package com.strategyobject.substrateclient.rpc.codegen.sections;

import com.strategyobject.substrateclient.rpc.GeneratedRpcSectionFactory;
import com.strategyobject.substrateclient.rpc.codegen.substitutes.TestSection;
import com.strategyobject.substrateclient.rpc.registries.RpcDecoderRegistry;
import com.strategyobject.substrateclient.rpc.registries.RpcEncoderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import com.strategyobject.substrateclient.transport.ProviderInterface;
import com.strategyobject.substrateclient.transport.RpcBoolean;
import com.strategyobject.substrateclient.transport.RpcObject;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RpcSectionFactoryTest {
    // TODO move this test out of the project
    @Test
    void createsRpcSectionAndCallsMethod() throws Exception {
        val expected = true;
        val sendFuture = CompletableFuture.completedFuture((RpcObject) new RpcBoolean(expected));
        val provider = mock(ProviderInterface.class);
        when(provider.send(anyString(), anyList())).thenReturn(sendFuture);

        try (val rpcGeneratedSectionFactory = new GeneratedRpcSectionFactory(
                provider,
                new RpcEncoderRegistry(),
                new ScaleWriterRegistry(),
                new RpcDecoderRegistry(),
                new ScaleReaderRegistry())) {
            val rpcSection = rpcGeneratedSectionFactory.create(TestSection.class);

            val actual = rpcSection.doNothing("some").get();

            assertEquals(expected, actual);
        }
    }
}
