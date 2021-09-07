package com.strategyobject.substrateclient.transport.ws;

import com.google.common.base.Strings;
import com.strategyobject.substrateclient.tests.containers.SubstrateVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ProviderInterfaceEmitted;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class WsProviderTest {
    private static final int WAIT_TIMEOUT = 10;

    @Container
    static TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0);

    @Test
    void canConnect() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {

            assertDoesNotThrow(() -> wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS));
            assertTrue(wsProvider.isConnected());
        }
    }

    @Test
    void connectFailsWhenConnected() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .build()) {

            val executionException = assertThrows(
                    ExecutionException.class,
                    () -> wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS));
            assertTrue(executionException.getCause() instanceof IllegalStateException);
        }
    }

    @Test
    void canAutoConnect() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .setAutoConnectDelay(5000)
                .build()) {

            assertDoesNotThrow(
                    () -> await()
                            .atMost(WAIT_TIMEOUT, TimeUnit.SECONDS)
                            .until(wsProvider::isConnected));
        }
    }

    @Test
    void notifiesWhenConnected() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {

            val notified = new AtomicBoolean();
            wsProvider.on(ProviderInterfaceEmitted.CONNECTED, _x -> notified.set(true));
            wsProvider.connect();

            assertDoesNotThrow(
                    () -> await()
                            .atMost(WAIT_TIMEOUT, TimeUnit.SECONDS)
                            .until(notified::get));
        }
    }

    @Test
    @SneakyThrows
    void canCancelNotification() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {

            val notified = new AtomicBoolean();
            val cancellation = wsProvider.on(ProviderInterfaceEmitted.CONNECTED, _x -> notified.set(true));
            cancellation.run();
            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertFalse(notified.get());
        }
    }

    @Test
    @SneakyThrows
    void canDisconnect() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {

            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            assertDoesNotThrow(wsProvider::disconnect);
            assertFalse(wsProvider.isConnected());
        }
    }

    @Test
    @SneakyThrows
    void notifiesWhenDisconnected() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {

            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            val notified = new AtomicBoolean();
            wsProvider.on(ProviderInterfaceEmitted.DISCONNECTED, _x -> notified.set(true));
            wsProvider.disconnect();

            assertDoesNotThrow(
                    () -> await()
                            .atMost(WAIT_TIMEOUT, TimeUnit.SECONDS)
                            .until(notified::get));
        }
    }

    @Test
    @SneakyThrows
    void canSend() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {

            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            val version = wsProvider.<String>send("system_version").get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            assertFalse(Strings.isNullOrEmpty(version));
        }
    }

    @Test
    @SneakyThrows
    void canSubscribe() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {

            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            val callbackCount = new AtomicInteger();
            val subId = wsProvider.subscribe(
                    "chain_newHead",
                    "chain_subscribeNewHeads",
                    null,
                    (ex, result) -> callbackCount.getAndIncrement()
            ).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertFalse(Strings.isNullOrEmpty(subId));
            await()
                    .atMost(WAIT_TIMEOUT, TimeUnit.SECONDS)
                    .until(() -> callbackCount.get() > 3);
        }
    }

    @Test
    @SneakyThrows
    void canUnsubscribe() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {

            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            val callbackCount = new AtomicInteger();
            val subId = wsProvider.subscribe(
                    "chain_newHead",
                    "chain_subscribeNewHeads",
                    null,
                    (ex, result) -> callbackCount.getAndIncrement()
            ).get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            await()
                    .atMost(WAIT_TIMEOUT, TimeUnit.SECONDS)
                    .until(() -> callbackCount.get() > 0);

            val success = wsProvider
                    .unsubscribe("chain_newHead", "chain_unsubscribeNewHeads", subId)
                    .get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            assertTrue(success);
        }
    }

    @Test
    @SneakyThrows
    void sendThrowsRpcExceptions() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {

            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            val executionException = assertThrows(
                    ExecutionException.class,
                    () -> wsProvider.send("unknown_method", null).get(WAIT_TIMEOUT, TimeUnit.SECONDS));
            assertTrue(executionException.getCause() instanceof RpcException);
        }
    }

    @Test
    void supportsSubscriptions() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {
            assertTrue(wsProvider.hasSubscriptions());
        }
    }
}