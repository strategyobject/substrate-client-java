package com.strategyobject.substrateclient.transport.ws;

import com.strategyobject.substrateclient.tests.containers.SubstrateVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ProviderInterfaceEmitted;
import eu.rekawek.toxiproxy.model.ToxicDirection;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.ToxiproxyContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class WsProviderProxyTest {
    static final Network network = Network.newNetwork();

    @Container
    static final TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0).waitingFor(Wait.forLogMessage(".*Running JSON-RPC WS server.*", 1))
            .withNetwork(network);

    @Container
    static final ToxiproxyContainer toxiproxy = new ToxiproxyContainer("shopify/toxiproxy")
            .withNetwork(network)
            .withNetworkAliases("toxiproxy");
    private static final int HEARTBEAT_INTERVAL = 5;
    private static final int RECONNECT_INTERVAL = 5;
    private static final int WAIT_TIMEOUT = HEARTBEAT_INTERVAL * 3;
    final ToxiproxyContainer.ContainerProxy proxy = toxiproxy.getProxy(substrate, 9944);

    @Test
    @SneakyThrows
    void canReconnect() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(getWsAddress())
                .setHeartbeatsInterval(HEARTBEAT_INTERVAL)
                .withPolicy(ReconnectionPolicy.exponentialBackoff()
                        .retryAfter(RECONNECT_INTERVAL, TimeUnit.SECONDS)
                        .withMaxDelay(RECONNECT_INTERVAL)
                        .build())
                .build()) {

            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            assertTrue(wsProvider.isConnected());

            proxy.setConnectionCut(true);
            await()
                    .atMost(WAIT_TIMEOUT, TimeUnit.SECONDS)
                    .until(() -> !wsProvider.isConnected());

            proxy.setConnectionCut(false);
            await()
                    .atMost(RECONNECT_INTERVAL * 2, TimeUnit.SECONDS)
                    .until(wsProvider::isConnected);
        }
    }

    @Test
    @SneakyThrows
    void canReconnectWhenConnectionWasClosedForALongPeriod() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(getWsAddress())
                .setHeartbeatsInterval(HEARTBEAT_INTERVAL)
                .withPolicy(ReconnectionPolicy.exponentialBackoff()
                        .retryAfter(RECONNECT_INTERVAL, TimeUnit.SECONDS)
                        .withMaxDelay(RECONNECT_INTERVAL)
                        .build())
                .build()) {

            val disconnectionCounter = new AtomicInteger(0);
            wsProvider.on(ProviderInterfaceEmitted.DISCONNECTED, i -> disconnectionCounter.incrementAndGet());
            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            assertTrue(wsProvider.isConnected());

            val CLOSE_AFTER = 1000;
            val toxic = proxy
                    .toxics()
                    .timeout("timeout", ToxicDirection.DOWNSTREAM, CLOSE_AFTER);
            await()
                    .atLeast(CLOSE_AFTER, TimeUnit.MILLISECONDS)
                    .atMost(HEARTBEAT_INTERVAL + RECONNECT_INTERVAL * 4, TimeUnit.SECONDS)
                    .untilAtomic(disconnectionCounter, greaterThan(2));
            assertFalse(wsProvider.isConnected());

            toxic.remove();
            await()
                    .atMost(RECONNECT_INTERVAL * 2, TimeUnit.SECONDS)
                    .until(wsProvider::isConnected);
        }
    }

    @Test
    @SneakyThrows
    void canAutoConnectWhenServerAvailable() {
        val closed = proxy
                .toxics()
                .limitData("closed", ToxicDirection.DOWNSTREAM, 0);

        try (val wsProvider = WsProvider.builder()
                .setEndpoint(getWsAddress())
                .disableHeartbeats()
                .withPolicy(ReconnectionPolicy.exponentialBackoff()
                        .retryAfter(RECONNECT_INTERVAL, TimeUnit.SECONDS)
                        .withMaxDelay(RECONNECT_INTERVAL)
                        .build())
                .build()) {

            val exception = assertThrows(
                    ExecutionException.class,
                    () -> wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS));
            assertTrue(exception.getCause() instanceof WsClosedException);
            assertFalse(wsProvider.isConnected());

            closed.remove();
            await()
                    .atMost(RECONNECT_INTERVAL * 2, TimeUnit.SECONDS)
                    .until(wsProvider::isConnected);
        }
    }

    @Test
    @SneakyThrows
    void throwsExceptionWhenCanNotSendRequestAndCleanHandler() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(getWsAddress())
                .withPolicy(ReconnectionPolicy.MANUAL)
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
                .withPolicy(ReconnectionPolicy.MANUAL)
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

    private <T> Map<?, ?> getHandlersOf(WsProvider wsProvider) throws NoSuchFieldException, IllegalAccessException {
        val handlersFields = wsProvider.getClass().getDeclaredField("handlers");
        handlersFields.setAccessible(true);

        return (Map<?, ?>) handlersFields.get(wsProvider);
    }

    private String getWsAddress() {
        return String.format("ws://%s:%s", proxy.getContainerIpAddress(), proxy.getProxyPort());
    }
}
