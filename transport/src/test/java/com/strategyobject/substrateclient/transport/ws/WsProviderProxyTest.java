package com.strategyobject.substrateclient.transport.ws;

import com.strategyobject.substrateclient.tests.containers.SubstrateVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import eu.rekawek.toxiproxy.model.ToxicDirection;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.ToxiproxyContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class WsProviderProxyTest {
    static final Network network = Network.newNetwork();

    @Container
    static final TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0)
            .withNetwork(network);

    @Container
    static final ToxiproxyContainer toxiproxy = new ToxiproxyContainer("shopify/toxiproxy")
            .withNetwork(network)
            .withNetworkAliases("toxiproxy");
    private static final int HEARTBEAT_INTERVAL = 5;
    private static final int WAIT_TIMEOUT = HEARTBEAT_INTERVAL * 2;
    final ToxiproxyContainer.ContainerProxy proxy = toxiproxy.getProxy(substrate, 9944);

    @Test
    void canReconnect() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(getWsAddress())
                .setHeartbeatsInterval(HEARTBEAT_INTERVAL)
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

    @Test
    @SneakyThrows
    void throwsExceptionWhenCanNotSendRequestAndCleanHandler() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(getWsAddress())
                .disableAutoConnect()
                .build()) {

            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            assertTrue(wsProvider.isConnected());

            val timeout = proxy
                    .toxics()
                    .timeout("timeout", ToxicDirection.UPSTREAM, 1000);

            val exception = assertThrows(CompletionException.class,
                    () -> wsProvider.send("system_version").join());
            assertTrue(exception.getCause() instanceof WsClosedException);

            val handlers = getHandlersOf(wsProvider);
            assertEquals(0, handlers.size());

            timeout.remove();
        }
    }

    @Test
    @SneakyThrows
    void throwsExceptionWhenResponseTimeoutAndCleanHandler() {
        val responseTimeout = 500;
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(getWsAddress())
                .setResponseTimeout(responseTimeout, TimeUnit.MILLISECONDS)
                .disableAutoConnect()
                .build()) {

            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            assertTrue(wsProvider.isConnected());

            val latency = proxy
                    .toxics()
                    .latency("latency", ToxicDirection.DOWNSTREAM, responseTimeout * 3);

            val exception = assertThrows(CompletionException.class,
                    () -> wsProvider.send("system_version").join(),
                    String.format("The node didn't respond for %s milliseconds.", responseTimeout));
            assertTrue(exception.getCause() instanceof TimeoutException);

            val handlers = getHandlersOf(wsProvider);
            assertEquals(0, handlers.size());

            latency.remove();
        }
    }

    private Map<?, ?> getHandlersOf(WsProvider wsProvider) throws NoSuchFieldException, IllegalAccessException {
        val handlersFields = wsProvider.getClass().getDeclaredField("handlers");
        handlersFields.setAccessible(true);

        return (Map<?, ?>) handlersFields.get(wsProvider);
    }

    private String getWsAddress() {
        return String.format("ws://%s:%s", proxy.getContainerIpAddress(), proxy.getProxyPort());
    }
}
