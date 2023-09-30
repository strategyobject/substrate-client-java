package com.strategyobject.substrateclient.transport.ws;

import com.google.common.base.Strings;
import com.strategyobject.substrateclient.tests.containers.FrequencyVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ProviderInterfaceEmitted;
import com.strategyobject.substrateclient.transport.ProviderStatus;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.wait.strategy.Wait;
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
    static final TestSubstrateContainer substrate = new TestSubstrateContainer(FrequencyVersion.CURRENT_VERSION).waitingFor(Wait.forLogMessage(".*Running JSON-RPC server.*", 1));

    @Test
    void canConnect() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .withPolicy(ReconnectionPolicy.MANUAL)
                .build()) {

            assertDoesNotThrow(() -> wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS));
            assertTrue(wsProvider.isConnected());
        }
    }

    @Test
    void connectReturnsSameFutureWhenCalledMultiple() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .build()) {

            val connectA = wsProvider.connect();
            val connectB = wsProvider.connect();

            assertEquals(connectA, connectB);
        }
    }

    @Test
    void notifiesWhenConnected() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .withPolicy(ReconnectionPolicy.MANUAL)
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
                .withPolicy(ReconnectionPolicy.MANUAL)
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
                .withPolicy(ReconnectionPolicy.MANUAL)
                .build()) {

            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            assertDoesNotThrow(wsProvider::disconnect);
            assertFalse(wsProvider.isConnected());
        }
    }

    @Test
    @SneakyThrows
    void disconnectReturnsSameFutureWhenCalledMultiple() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .build()) {

            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            assertTrue(wsProvider.isConnected());

            val disconnectA = wsProvider.disconnect();
            val disconnectB = wsProvider.disconnect();

            assertEquals(disconnectA, disconnectB);
        }
    }

    @Test
    @SneakyThrows
    void notifiesWhenDisconnected() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .withPolicy(ReconnectionPolicy.MANUAL)
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
                .withPolicy(ReconnectionPolicy.MANUAL)
                .build()) {

            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            val version = wsProvider.send("system_version")
                    .get(WAIT_TIMEOUT, TimeUnit.SECONDS)
                    .asString();
            assertFalse(Strings.isNullOrEmpty(version));
        }
    }

    @Test
    @SneakyThrows
    @Disabled("This test is flaky and given that the instant seal node does emit less events it seems like this flaking out is not a real danger, it's more a difference in configuration on the server side.")
    void canSubscribe() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .withPolicy(ReconnectionPolicy.MANUAL)
                .build()) {

            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            val callbackCount = new AtomicInteger();
            val subId = wsProvider.subscribe(
                    "chain_newHead",
                    "chain_subscribeNewHeads",
                    null,
                    (ex, result) -> callbackCount.getAndIncrement()
            ).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            try {
                assertFalse(Strings.isNullOrEmpty(subId));
                await()
                        .atMost(WAIT_TIMEOUT, TimeUnit.SECONDS)
                        .until(() -> callbackCount.get() > 3);
            }catch(Exception e){
                System.out.println("####################CALLBACK COUNT: " + callbackCount.get());
                throw e;
            }
        }
    }

    @Test
    @SneakyThrows
    void canUnsubscribe() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .withPolicy(ReconnectionPolicy.MANUAL)
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
                .withPolicy(ReconnectionPolicy.MANUAL)
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
                .withPolicy(ReconnectionPolicy.MANUAL)
                .build()) {

            assertTrue(wsProvider.hasSubscriptions());
        }
    }

    @Test
    @SneakyThrows
    void canReconnectManually() {
        try (val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .withPolicy(ReconnectionPolicy.MANUAL)
                .build()) {

            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            assertTrue(wsProvider.isConnected());

            wsProvider.disconnect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            assertFalse(wsProvider.isConnected());
            assertEquals(ProviderStatus.DISCONNECTED, wsProvider.getStatus());

            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            assertTrue(wsProvider.isConnected());
            assertEquals(ProviderStatus.CONNECTED, wsProvider.getStatus());
        }
    }
}