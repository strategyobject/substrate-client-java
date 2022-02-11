package com.strategyobject.substrateclient.transport.ws;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.strategyobject.substrateclient.common.eventemitter.EventEmitter;
import com.strategyobject.substrateclient.common.eventemitter.EventListener;
import com.strategyobject.substrateclient.transport.*;
import com.strategyobject.substrateclient.transport.coder.JsonRpcResponse;
import com.strategyobject.substrateclient.transport.coder.JsonRpcResponseSingle;
import com.strategyobject.substrateclient.transport.coder.JsonRpcResponseSubscription;
import com.strategyobject.substrateclient.transport.coder.RpcCoder;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.CloseFrame;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiConsumer;

@Getter
@Setter
class WsStateSubscription extends SubscriptionHandler {
    private String method;
    private List<Object> params;

    public WsStateSubscription(BiConsumer<Exception, RpcObject> callBack,
                               String type,
                               String method,
                               List<Object> params) {
        super(callBack, type);
        this.method = method;
        this.params = params;
    }
}

@Getter
@Setter
@AllArgsConstructor
class WsStateAwaiting {
    private CompletableFuture<RpcObject> callback;
    private String method;
    private List<Object> params;
    private SubscriptionHandler subscription;
}

@Slf4j
public class WsProvider implements ProviderInterface, AutoCloseable {
    private static final int RESUBSCRIBE_TIMEOUT = 20;
    private static final Map<String, String> ALIASES = new HashMap<>();
    private static final ScheduledExecutorService timedOutHandlerCleaner;

    static {
        timedOutHandlerCleaner = Executors.newScheduledThreadPool(1);
        ALIASES.put("chain_finalisedHead", "chain_finalizedHead");
        ALIASES.put("chain_subscribeFinalisedHeads", "chain_subscribeFinalizedHeads");
        ALIASES.put("chain_unsubscribeFinalisedHeads", "chain_unsubscribeFinalizedHeads");
    }

    private final RpcCoder coder = new RpcCoder();
    private final URI endpoint;
    private final Map<String, String> headers;
    private final EventEmitter eventEmitter = new EventEmitter();
    private final Map<Integer, WsStateAwaiting> handlers = new ConcurrentHashMap<>();
    private final Map<String, WsStateSubscription> subscriptions = new ConcurrentHashMap<>();
    private final Map<String, JsonRpcResponseSubscription> waitingForId = new ConcurrentHashMap<>();
    private final int heartbeatInterval;
    private final long responseTimeoutInMs;
    private volatile int autoConnectMs;
    private volatile WebSocketClient webSocket = null;
    private volatile CompletableFuture<Void> whenConnected = null;
    private volatile CompletableFuture<Void> whenDisconnected = null;
    private volatile ProviderStatus status = ProviderStatus.DISCONNECTED;

    WsProvider(@NonNull URI endpoint,
               int autoConnectMs,
               Map<String, String> headers,
               int heartbeatInterval,
               long responseTimeoutInMs) {
        Preconditions.checkArgument(
                endpoint.getScheme().matches("(?i)ws|wss"),
                "Endpoint should start with 'ws://', received " + endpoint);
        Preconditions.checkArgument(
                autoConnectMs >= 0,
                "AutoConnect delay cannot be less than 0");

        this.endpoint = endpoint;
        this.autoConnectMs = autoConnectMs;
        this.headers = headers;
        this.heartbeatInterval = heartbeatInterval;
        this.responseTimeoutInMs = responseTimeoutInMs;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * {@inheritDoc}
     *
     * @return always returns true
     */
    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    @Override
    public ProviderStatus getStatus() {
        return this.status;
    }

    /**
     * {@inheritDoc}
     *
     * @return true if connected
     */
    @Override
    public boolean isConnected() {
        return this.status == ProviderStatus.CONNECTED;
    }

    /**
     * {@inheritDoc}
     * <p> The {@link com.strategyobject.substrateclient.transport.ws.WsProvider} connects automatically by default,
     * however if you decided otherwise, you may connect manually using this method.
     */
    public synchronized CompletableFuture<Void> connect() {
        val currentStatus = this.status;
        Preconditions.checkState(
                currentStatus == ProviderStatus.DISCONNECTED || currentStatus == ProviderStatus.CONNECTING,
                "WebSocket is already connected");

        var inProgress = this.whenConnected;
        if (inProgress != null) {
            return inProgress;
        }

        this.status = ProviderStatus.CONNECTING;

        val whenConnected = new CompletableFuture<Void>();
        this.whenConnected = whenConnected;

        try {
            val ws = WebSocket.builder()
                    .setServerUri(this.endpoint)
                    .setHttpHeaders(this.headers)
                    .onClose(this::onSocketClose)
                    .onError(this::onSocketError)
                    .onMessage(this::onSocketMessage)
                    .onOpen(this::onSocketOpen)
                    .build();
            ws.setConnectionLostTimeout(this.heartbeatInterval);

            this.webSocket = ws;
            this.eventEmitter.once(ProviderInterfaceEmitted.CONNECTED, _x -> {
                whenConnected.complete(null);
                this.whenConnected = null;
            });
            ws.connect();
        } catch (Exception ex) {
            log.error("Connect error", ex);
            whenConnected.completeExceptionally(ex);
            this.emit(ProviderInterfaceEmitted.ERROR, ex);
            this.whenConnected = null;
            this.status = ProviderStatus.DISCONNECTED;
        }

        return whenConnected;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized CompletableFuture<Void> disconnect() {
        val currentStatus = this.status;
        var inProgress = this.whenDisconnected;

        Preconditions.checkState(
                currentStatus == ProviderStatus.CONNECTED ||
                        (currentStatus == ProviderStatus.DISCONNECTING && inProgress != null),
                "WebSocket is not connected");

        if (inProgress != null) {
            return inProgress;
        }

        val whenDisconnected = new CompletableFuture<Void>();
        this.whenDisconnected = whenDisconnected;

        // switch off autoConnect, we are in manual mode now
        this.autoConnectMs = 0;
        this.status = ProviderStatus.DISCONNECTING;

        val ws = this.webSocket;
        if (ws != null) {
            ws.close(CloseFrame.NORMAL);
        }

        return whenDisconnected;
    }

    /**
     * {@inheritDoc}
     *
     * @param type Event
     * @param sub  Callback
     * @return unsubscribe function
     */
    @Override
    public Runnable on(ProviderInterfaceEmitted type, EventListener sub) {
        this.eventEmitter.on(type, sub);

        return () -> this.eventEmitter.removeListener(type, sub);
    }

    private CompletableFuture<RpcObject> send(String method,
                                              List<Object> params,
                                              SubscriptionHandler subscription) {
        val ws = this.webSocket;
        Preconditions.checkState(
                ws != null && this.isConnected(),
                "WebSocket is not connected");

        val jsonRpcRequest = this.coder.encodeObject(method, params);
        val json = RpcCoder.encodeJson(jsonRpcRequest);
        val id = jsonRpcRequest.getId();
        log.debug("Calling {} {}, {}, {}, {}", id, method, params, json, subscription);

        val whenResponseReceived = new CompletableFuture<RpcObject>();
        this.handlers.put(id, new WsStateAwaiting(whenResponseReceived, method, params, subscription));

        return CompletableFuture.runAsync(() -> ws.send(json))
                .whenCompleteAsync((_res, ex) -> {
                    if (ex != null) {
                        this.handlers.remove(id);
                        whenResponseReceived.completeExceptionally(ex);
                    } else {
                        scheduleCleanupIfNoResponseWithinTimeout(id);
                    }
                })
                .thenCombineAsync(whenResponseReceived, (_a, b) -> b);
    }

    /**
     * Send JSON data using WebSockets to configured endpoint
     *
     * @param method The RPC methods to execute
     * @param params Encoded parameters as applicable for the method
     * @return future containing result
     */
    @Override
    public CompletableFuture<RpcObject> send(String method, List<Object> params) {
        return send(method, params, null);
    }

    /**
     * Send JSON data using WebSockets to configured endpoint
     *
     * @param method The RPC methods to execute
     * @return future containing result
     */
    @Override
    public CompletableFuture<RpcObject> send(String method) {
        return send(method, null, null);
    }

    /**
     * {@inheritDoc}
     *
     * @param type     Subscription type
     * @param method   The RPC methods to execute
     * @param params   Encoded parameters as applicable for the method
     * @param callback Callback
     * @return future containing subscription id
     */
    public CompletableFuture<String> subscribe(String type,
                                               String method,
                                               List<Object> params,
                                               BiConsumer<Exception, RpcObject> callback) {
        return this.send(method, params, new SubscriptionHandler(callback, type))
                .thenApplyAsync(RpcObject::asString);
    }

    /**
     * {@inheritDoc}
     *
     * @param type   Subscription type
     * @param method The RPC methods to execute
     * @param id     Subscription id
     * @return true if unsubscribed
     */
    @Override
    public CompletableFuture<Boolean> unsubscribe(String type, String method, String id) {
        val subscription = type + "::" + id;
        val whenUnsubscribed = new CompletableFuture<Boolean>();

        // FIXME This now could happen with re-subscriptions. The issue is that with a re-sub
        // the assigned id now does not match what the API user originally received. It has
        // a slight complication in solving - since we cannot rely on the sent id, but rather
        // need to find the actual subscription id to map it
        if (this.subscriptions.get(subscription) == null) {
            log.info("Unable to find active subscription={}", subscription);

            whenUnsubscribed.complete(false);
        } else {
            this.subscriptions.remove(subscription);
            if (this.isConnected() && this.webSocket != null) {
                return this.send(method, Collections.singletonList(id), null)
                        .thenApplyAsync(RpcObject::asBoolean);
            }

            whenUnsubscribed.complete(true);
        }

        return whenUnsubscribed;
    }

    private void scheduleCleanupIfNoResponseWithinTimeout(int id) {
        timedOutHandlerCleaner.schedule(() -> {
                    val handler = this.handlers.remove(id);
                    if (handler == null) {
                        return;
                    }

                    handler
                            .getCallback()
                            .completeExceptionally(new TimeoutException(
                                    String.format("The node didn't respond within %s milliseconds.",
                                            responseTimeoutInMs)));
                },
                responseTimeoutInMs,
                TimeUnit.MILLISECONDS);
    }

    private void emit(ProviderInterfaceEmitted type, Object... args) {
        this.eventEmitter.emit(type, args);
    }

    private synchronized void onSocketClose(int code, String reason) {
        val currentStatus = this.status;
        if (currentStatus == ProviderStatus.CONNECTED || currentStatus == ProviderStatus.CONNECTING) {
            this.status = ProviderStatus.DISCONNECTING;
        }

        if (Strings.isNullOrEmpty(reason)) {
            reason = ErrorCodes.getWSErrorString(code);
        }

        val ws = this.webSocket;
        val errorMessage = String.format(
                "Disconnected from %s code: '%s' reason: '%s'",
                ws == null ? this.endpoint : ws.getURI(),
                code,
                reason);

        if (this.autoConnectMs > 0) {
            log.error(errorMessage);
        }

        // reject all hanging requests
        val wsClosedException = new WsClosedException(errorMessage);
        this.handlers.values().forEach(x -> x.getCallback().completeExceptionally(wsClosedException));
        this.handlers.clear();
        this.waitingForId.clear();

        this.webSocket = null;
        this.status = ProviderStatus.DISCONNECTED;
        this.emit(ProviderInterfaceEmitted.DISCONNECTED);
        val whenDisconnected = this.whenDisconnected;
        if (whenDisconnected != null) {
            whenDisconnected.complete(null);
            this.whenDisconnected = null;
        }

        if (this.autoConnectMs > 0) {
            log.info("Trying to reconnect to {}", this.endpoint);
            this.connect();
        }
    }

    private void onSocketError(Exception ex) {
        log.error("WebSocket error", ex);
        this.emit(ProviderInterfaceEmitted.ERROR, ex);
    }

    private void onSocketMessage(String message) {
        log.debug("Received {}", message);

        JsonRpcResponse response = RpcCoder.decodeJson(message);
        if (Strings.isNullOrEmpty(response.getMethod())) {
            this.onSocketMessageResult(JsonRpcResponseSingle.from(response));
        } else {
            this.onSocketMessageSubscribe(JsonRpcResponseSubscription.from(response));
        }
    }

    private void onSocketMessageResult(JsonRpcResponseSingle response) {
        val id = response.getId();
        val handler = (WsStateAwaiting) this.handlers.get(id);
        if (handler == null) {
            log.error("Unable to find handler for id={}", id);
            return;
        }

        try {
            val result = response.getResult();
            // first send the result - in case of subs, we may have an update
            // immediately if we have some queued results already
            handler.getCallback().complete(result);

            val subscription = handler.getSubscription();
            if (subscription != null) {
                val subId = subscription.getType() + "::" + result.asString();
                this.subscriptions.put(
                        subId,
                        new WsStateSubscription(
                                subscription.getCallBack(),
                                subscription.getType(),
                                handler.getMethod(),
                                handler.getParams()));

                // if we have a result waiting for this subscription already
                val waiting = this.waitingForId.get(subId);
                if (waiting != null) {
                    this.onSocketMessageSubscribe(waiting);
                }
            }
        } catch (Exception ex) {
            handler.getCallback().completeExceptionally(ex);
        }

        this.handlers.remove(id);
    }

    private void onSocketMessageSubscribe(JsonRpcResponseSubscription response) {
        val method = ALIASES.getOrDefault(response.getMethod(), response.getMethod());
        val subId = method + "::" + response.getParams().getSubscription();

        log.debug("Handling: response =', {}, 'subscription =', {}", response, subId);

        val handler = this.subscriptions.get(subId);
        if (handler == null) {
            // store the JSON, we could have out-of-order subid coming in
            this.waitingForId.put(subId, response);
            log.info("Unable to find handler for subscription={}", subId);
            return;
        }

        // housekeeping
        this.waitingForId.remove(subId);

        try {
            val result = response.getResult();
            handler.getCallBack().accept(null, result);
        } catch (Exception ex) {
            handler.getCallBack().accept(ex, null);
        }
    }

    public synchronized void onSocketOpen() {
        log.info("Connected to: {}", this.webSocket.getURI());

        this.status = ProviderStatus.CONNECTED;
        this.emit(ProviderInterfaceEmitted.CONNECTED);
        this.resubscribe();
    }

    @Override
    public void close() {
        try {
            val currentStatus = this.status;
            if (currentStatus == ProviderStatus.CONNECTED || currentStatus == ProviderStatus.DISCONNECTING) {
                this.disconnect();
            }
        } catch (Exception ex) {
            log.error("Error while automatic closing", ex);
        }
    }

    private void resubscribe() {
        Map<String, WsStateSubscription> subscriptions = new HashMap<>(this.subscriptions);

        this.subscriptions.clear();

        try {
            CompletableFuture.allOf(
                    subscriptions.values()
                            .stream()
                            // only re-create subscriptions which are not in author (only area where
                            // transactions are created, i.e. submissions such as 'author_submitAndWatchExtrinsic'
                            // are not included (and will not be re-broadcast)
                            .filter(subscription -> !subscription.getType().startsWith("author_"))
                            .map(subscription -> {
                                try {
                                    return this.subscribe(
                                            subscription.getType(),
                                            subscription.getMethod(),
                                            subscription.getParams(),
                                            subscription.getCallBack());
                                } catch (Exception ex) {
                                    log.error("Resubscribe error {}", subscription, ex);
                                    return null;
                                }
                            })
                            .filter(Objects::nonNull)
                            .toArray(CompletableFuture<?>[]::new)
            ).get(RESUBSCRIBE_TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            log.error("Resubscribe error", ex);
        }
    }

    public static class Builder {
        private URI endpoint;
        private int autoConnectMs = 2500;
        private Map<String, String> headers = null;
        private int heartbeatInterval = 30;
        private long responseTimeoutInMs = 20000;

        Builder() {
            try {
                endpoint = new URI("ws://127.0.0.1:9944");
            } catch (URISyntaxException ex) {
                ex.printStackTrace();
            }
        }

        public Builder setEndpoint(@NonNull URI endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder setEndpoint(@NonNull String endpoint) {
            try {
                return setEndpoint(new URI(endpoint));
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException(ex);
            }
        }

        public Builder setAutoConnectDelay(int autoConnectMs) {
            this.autoConnectMs = autoConnectMs;
            return this;
        }

        public Builder disableAutoConnect() {
            this.autoConnectMs = 0;
            return this;
        }

        public Builder setHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder setHeartbeatsInterval(int heartbeatInterval) {
            this.heartbeatInterval = heartbeatInterval;
            return this;
        }

        public Builder disableHeartbeats() {
            this.heartbeatInterval = 0;
            return this;
        }

        public Builder setResponseTimeout(long timeout, TimeUnit timeUnit) {
            this.responseTimeoutInMs = timeUnit.toMillis(timeout);
            return this;
        }

        public WsProvider build() {
            return new WsProvider(this.endpoint,
                    this.autoConnectMs,
                    this.headers,
                    this.heartbeatInterval,
                    this.responseTimeoutInMs);
        }
    }
}
