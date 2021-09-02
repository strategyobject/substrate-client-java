package com.strategyobject.substrateclient.rpc.provider.ws;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.strategyobject.substrateclient.eventemitter.EventEmitter;
import com.strategyobject.substrateclient.eventemitter.EventListener;
import com.strategyobject.substrateclient.gc.WeakReferenceFinalizer;
import com.strategyobject.substrateclient.rpc.provider.ProviderInterface;
import com.strategyobject.substrateclient.rpc.provider.ProviderInterfaceEmitted;
import com.strategyobject.substrateclient.rpc.provider.SubscriptionHandler;
import com.strategyobject.substrateclient.rpc.provider.coder.JsonRpcResponse;
import com.strategyobject.substrateclient.rpc.provider.coder.JsonRpcResponseSingle;
import com.strategyobject.substrateclient.rpc.provider.coder.JsonRpcResponseSubscription;
import com.strategyobject.substrateclient.rpc.provider.coder.RpcCoder;
import lombok.*;
import org.java_websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiConsumer;

@Getter
@Setter
class WsStateSubscription<T> extends SubscriptionHandler<T> {
    private String method;
    private List<Object> params;

    public WsStateSubscription(BiConsumer<Exception, T> callBack,
                               String type,
                               Type resultType,
                               String method,
                               List<Object> params) {
        super(callBack, type, resultType);
        this.method = method;
        this.params = params;
    }
}

@AllArgsConstructor
@Getter
@Setter
class WsStateAwaiting<R, S> {
    private WeakReference<CompletableFuture<R>> callBack;
    private String method;
    private List<Object> params;
    private SubscriptionHandler<S> subscription;
    private Type resultType;
}

public class WsProvider implements ProviderInterface, AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(WsProvider.class);
    private static final int RESUBSCRIBE_TIMEOUT = 20;
    private final ReferenceQueue<CompletableFuture<?>> referenceQueue = new ReferenceQueue<>();

    private static final Map<String, String> ALIASES = new HashMap<>();

    private final RpcCoder coder = new RpcCoder();
    private final URI endpoint;
    private final Map<String, String> headers;
    private final EventEmitter eventEmitter = new EventEmitter();
    private final Map<Integer, WsStateAwaiting<?, ?>> handlers = new ConcurrentHashMap<>();
    private final Map<String, WsStateSubscription<?>> subscriptions = new ConcurrentHashMap<>();
    private final Map<String, JsonRpcResponseSubscription<?>> waitingForId = new ConcurrentHashMap<>();
    private final int heartbeatInterval;
    private int autoConnectMs;
    private boolean isConnected = false;
    private WebSocketClient webSocket;

    static {
        ALIASES.put("chain_finalisedHead", "chain_finalizedHead");
        ALIASES.put("chain_subscribeFinalisedHeads", "chain_subscribeFinalizedHeads");
        ALIASES.put("chain_unsubscribeFinalisedHeads", "chain_unsubscribeFinalizedHeads");
    }

    WsProvider(@NonNull URI endpoint,
               int autoConnectMs,
               Map<String, String> headers,
               int heartbeatInterval) {
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

        if (autoConnectMs > 0) {
            this.connect();
        }
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


    /**
     * {@inheritDoc}
     *
     * @return true if connected
     */
    @Override
    public boolean isConnected() {
        return this.isConnected;
    }


    /**
     * {@inheritDoc}
     * <p> The {@link com.strategyobject.substrateclient.rpc.provider.ws.WsProvider} connects automatically by default,
     * however if you decided otherwise, you may connect manually using this method.
     */
    public CompletableFuture<Void> connect() {
        Preconditions.checkState(this.webSocket == null);

        val whenConnected = new CompletableFuture<Void>();

        try {
            this.webSocket = WebSocket.builder()
                    .setServerUri(this.endpoint)
                    .setHttpHeaders(this.headers)
                    .onClose(this::onSocketClose)
                    .onError(this::onSocketError)
                    .onMessage(this::onSocketMessage)
                    .onOpen(this::onSocketOpen)
                    .build();

            this.webSocket.setConnectionLostTimeout(this.heartbeatInterval);

            this.eventEmitter.once(ProviderInterfaceEmitted.CONNECTED, _x -> whenConnected.complete(null));
            this.webSocket.connect();
        } catch (Exception ex) {
            logger.error("Connect error", ex);
            this.emit(ProviderInterfaceEmitted.ERROR, ex);
            whenConnected.completeExceptionally(ex);
        }

        return whenConnected;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() {
        this.isConnected = false;
        // switch off autoConnect, we are in manual mode now
        this.autoConnectMs = 0;

        try {
            if (this.webSocket != null) {
                // 1000 - Normal closure; the connection successfully completed
                this.webSocket.close(1000);
                this.webSocket = null;
            }
        } catch (Exception ex) {
            logger.error("Error disconnecting", ex);
            this.emit(ProviderInterfaceEmitted.ERROR, ex);
            throw ex;
        }
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

    private <R, S> CompletableFuture<R> send(String method,
                                             List<Object> params,
                                             SubscriptionHandler<S> subscription) {
        Preconditions.checkState(
                this.webSocket != null && this.isConnected,
                "WebSocket is not connected");

        val jsonRpcRequest = this.coder.encodeObject(method, params);
        val json = RpcCoder.encodeJson(jsonRpcRequest);
        val id = jsonRpcRequest.getId();

        logger.debug("Calling {} {}, {}, {}, {}", id, method, params, json, subscription);

        this.webSocket.send(json);

        val whenResponseReceived = new CompletableFuture<R>();
        val callback = new WeakReferenceFinalizer<>(
                whenResponseReceived,
                referenceQueue,
                () -> this.handlers.remove(id));

        val resultType = RpcCoder.<R>getResultType();
        this.handlers.put(id, new WsStateAwaiting<>(callback, method, params, subscription, resultType));

        return whenResponseReceived;
    }

    /**
     * Send JSON data using WebSockets to configured endpoint
     *
     * @param method The RPC methods to execute
     * @param params Encoded parameters as applicable for the method
     * @param <T>    Type of the result
     * @return future containing result
     */
    @Override
    public <T> CompletableFuture<T> send(String method, List<Object> params) {
        return send(method, params, null);
    }

    /**
     * Send JSON data using WebSockets to configured endpoint
     *
     * @param method The RPC methods to execute
     * @param <T>    Type of the result
     * @return future containing result
     */
    @Override
    public <T> CompletableFuture<T> send(String method) {
        return send(method, null, null);
    }

    /**
     * {@inheritDoc}
     *
     * @param type     Subscription type
     * @param method   The RPC methods to execute
     * @param params   Encoded parameters as applicable for the method
     * @param callback Callback
     * @param <T>
     * @return future containing subscription id
     */
    public <T> CompletableFuture<String> subscribe(String type,
                                                   String method,
                                                   List<Object> params,
                                                   BiConsumer<Exception, T> callback) {
        val resultType = RpcCoder.<T>getResultType();

        return this.send(method, params, new SubscriptionHandler<>(callback, type, resultType));
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
            logger.info("Unable to find active subscription={}", subscription);

            whenUnsubscribed.complete(false);
        } else {
            this.subscriptions.remove(subscription);
            if (this.isConnected() && this.webSocket != null) {
                return this.send(method, Collections.singletonList(id), null);
            }

            whenUnsubscribed.complete(true);
        }

        return whenUnsubscribed;
    }

    private void emit(ProviderInterfaceEmitted type, Object... args) {
        this.eventEmitter.emit(type, args);
    }

    private void onSocketClose(int code, String reason) {
        if (Strings.isNullOrEmpty(reason)) {
            reason = ErrorCodes.getWSErrorString(code);
        }

        val errorMessage = String.format(
                "Disconnected from %s code: '%s' reason: '%s'",
                this.webSocket == null ? this.endpoint : this.webSocket.getURI(),
                code,
                reason);

        if (this.autoConnectMs > 0) {
            logger.error(errorMessage);
        }

        this.isConnected = false;
        this.webSocket = null;
        this.emit(ProviderInterfaceEmitted.DISCONNECTED);

        // reject all hanging requests
        val wsClosedException = new WsClosedException(errorMessage);
        this.handlers.values().forEach(x -> {
            val callback = x.getCallBack().get();
            if (callback != null) {
                callback.completeExceptionally(wsClosedException);
            }
        });
        this.handlers.clear();
        this.waitingForId.clear();

        if (this.autoConnectMs > 0) {
            logger.info("Trying to reconnect to {}", this.endpoint);
            this.connect();
        }
    }

    private void onSocketError(Exception ex) {
        logger.error("WebSocket error", ex);
        this.emit(ProviderInterfaceEmitted.ERROR, ex);
    }

    private void onSocketMessage(String message) {
        logger.debug("Received {}", message);
        this.cleanCollectedHandlers();

        JsonRpcResponse response = RpcCoder.decodeJson(message);
        if (Strings.isNullOrEmpty(response.getMethod())) {
            this.onSocketMessageResult(JsonRpcResponseSingle.from(response));
        } else {
            this.onSocketMessageSubscribe(JsonRpcResponseSubscription.from(response));
        }
    }

    @SuppressWarnings("unchecked")
    private <R, S> void onSocketMessageResult(JsonRpcResponseSingle<R> response) {
        val id = response.getId();
        val handler = (WsStateAwaiting<R, S>) this.handlers.get(id);
        if (handler == null) {
            logger.error("Unable to find handler for id={}", id);
            return;
        }

        val callback = Optional.ofNullable(handler.getCallBack().get());
        try {
            val result = response.getResult(handler.getResultType());
            // first send the result - in case of subs, we may have an update
            // immediately if we have some queued results already
            callback.ifPresent(x -> x.complete(result));

            val subscription = handler.getSubscription();
            if (subscription != null) {
                val subId = subscription.getType() + "::" + result;
                this.subscriptions.put(
                        subId,
                        new WsStateSubscription<>(
                                subscription.getCallBack(),
                                subscription.getType(),
                                subscription.getResultType(),
                                handler.getMethod(),
                                handler.getParams()));

                // if we have a result waiting for this subscription already
                val waiting = this.waitingForId.get(subId);
                if (waiting != null) {
                    this.onSocketMessageSubscribe(waiting);
                }
            }
        } catch (Exception ex) {
            callback.ifPresent(x -> x.completeExceptionally(ex));
        }

        this.handlers.remove(id);
    }

    @SuppressWarnings("unchecked")
    private <T> void onSocketMessageSubscribe(JsonRpcResponseSubscription<T> response) {
        val method = ALIASES.getOrDefault(response.getMethod(), response.getMethod());
        val subId = method + "::" + response.getParams().getSubscription();

        logger.debug("Handling: response =', {}, 'subscription =', {}", response, subId);

        val handler = (WsStateSubscription<T>) this.subscriptions.get(subId);
        if (handler == null) {
            // store the JSON, we could have out-of-order subid coming in
            this.waitingForId.put(subId, response);
            logger.info("Unable to find handler for subscription={}", subId);
            return;
        }

        // housekeeping
        this.waitingForId.remove(subId);

        try {
            val result = response.getResult(handler.getResultType());
            handler.getCallBack().accept(null, result);
        } catch (Exception ex) {
            handler.getCallBack().accept(ex, null);
        }
    }

    public void onSocketOpen() {
        logger.info("Connected to: {}", this.webSocket.getURI());

        this.isConnected = true;
        this.emit(ProviderInterfaceEmitted.CONNECTED);
        this.resubscribe();
    }

    @Override
    public void close() {
        this.disconnect();
    }

    private void resubscribe() {
        Map<String, WsStateSubscription<?>> subscriptions = new HashMap<>(this.subscriptions);

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
                                    logger.error("Resubscribe error {}", subscription, ex);
                                    return null;
                                }
                            })
                            .filter(Objects::nonNull)
                            .toArray(CompletableFuture<?>[]::new)
            ).get(RESUBSCRIBE_TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            logger.error("Resubscribe error", ex);
        }
    }

    private void cleanCollectedHandlers() {
        Reference<?> referenceFromQueue;
        while ((referenceFromQueue = referenceQueue.poll()) != null) {
            ((WeakReferenceFinalizer<?>) referenceFromQueue).finalizeResources();
            referenceFromQueue.clear();
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private URI endpoint;
        private int autoConnectMs = 2500;
        private Map<String, String> headers = null;
        private int heartbeatInterval = 60;

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

        public WsProvider build() {
            return new WsProvider(this.endpoint, this.autoConnectMs, this.headers, this.heartbeatInterval);
        }
    }
}
