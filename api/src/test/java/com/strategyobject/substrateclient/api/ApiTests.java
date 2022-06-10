package com.strategyobject.substrateclient.api;

import com.strategyobject.substrateclient.tests.containers.SubstrateVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ws.WsProvider;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
class ApiTests {
    private static final int WAIT_TIMEOUT = 1000;

    @Container
    private final TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0);

    @Test
    void getSystemPalletAndCall() throws Exception { // TODO move the test out of the project
        val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .build();
        wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

        try (val api = Api.with(wsProvider)) {
            val systemPallet = api.pallet(SystemPallet.class);
            val blockHash = systemPallet
                    .blockHash()
                    .get(0)
                    .get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertNotNull(blockHash);
            assertNotEquals(BigInteger.ZERO, new BigInteger(blockHash.getData()));
        }
    }
}
