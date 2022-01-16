package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.rpc.codegen.sections.RpcGeneratedSectionFactory;
import com.strategyobject.substrateclient.rpc.codegen.sections.RpcInterfaceInitializationException;
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

@Testcontainers
public class SystemTests {
    private static final int WAIT_TIMEOUT = 10;
    private static final Network network = Network.newNetwork();

    @Container
    static final TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0).withNetwork(network);

    @Test
    void accountNextIndex() throws ExecutionException, InterruptedException, TimeoutException, RpcInterfaceInitializationException {
        try (WsProvider wsProvider = WsProvider.builder().setEndpoint(substrate.getWsAddress()).disableAutoConnect().build()) {
            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            val sectionFactory = new RpcGeneratedSectionFactory();
            System rpcSection = sectionFactory.create(System.class, wsProvider);

            val alicePublicKey = HexConverter.toBytes("0xd43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d");
            val result = rpcSection.accountNextIndex(AccountId.fromBytes(alicePublicKey))
                    .get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertEquals(0, result);
        }
    }
}
