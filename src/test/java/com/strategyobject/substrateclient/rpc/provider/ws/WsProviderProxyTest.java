package com.strategyobject.substrateclient.rpc.provider.ws;

import com.strategyobject.substrateclient.utils.SubstrateVersion;
import com.strategyobject.substrateclient.utils.TestSubstrateContainer;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.ToxiproxyContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Testcontainers
public class WsProviderProxyTest {
    static Network network = Network.newNetwork();

    @Container
    static TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0)
            .withNetwork(network);

    @Container
    static ToxiproxyContainer toxiproxy = new ToxiproxyContainer("shopify/toxiproxy")
            .withNetwork(network)
            .withNetworkAliases("toxiproxy");

    final ToxiproxyContainer.ContainerProxy proxy = toxiproxy.getProxy(substrate, 9944);

    private static final int HEARTBEAT_INTERVAL = 5;
    private static final int WAIT_TIMEOUT = HEARTBEAT_INTERVAL * 2;


    @Test
    void canReconnect() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(getWsAddress())
                .setHeartbeatInterval(HEARTBEAT_INTERVAL)
                .build()) {

            await()
                    .atMost(WAIT_TIMEOUT, TimeUnit.SECONDS)
                    .until(wsProvider::isConnected);

            proxy.setConnectionCut(true);
            await()
                    .atMost(WAIT_TIMEOUT, TimeUnit.SECONDS)
                    .until(() -> !wsProvider.isConnected());

            proxy.setConnectionCut(false);
            await()
                    .atMost(WAIT_TIMEOUT, TimeUnit.SECONDS)
                    .until(wsProvider::isConnected);
        }
    }

    @Test
    @SneakyThrows
    void canAutoConnectWhenServerAvailable() {
        proxy.setConnectionCut(true);

        try (val wsProvider = WsProvider.builder()
                .setEndpoint(getWsAddress())
                .disableHeartbeats()
                .build()) {

            Thread.sleep(WAIT_TIMEOUT * 1000);
            assertFalse(wsProvider.isConnected());

            proxy.setConnectionCut(false);
            await()
                    .atMost(WAIT_TIMEOUT, TimeUnit.SECONDS)
                    .until(wsProvider::isConnected);
        }
    }

    private String getWsAddress() {
        return String.format("ws://%s:%s", proxy.getContainerIpAddress(), proxy.getProxyPort());
    }
}
