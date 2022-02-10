package com.strategyobject.substrateclient.transport;

import com.strategyobject.substrateclient.common.eventemitter.EventListener;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public interface ProviderInterface {
    /**
     * Whether the provider support subscriptions or not
     *
     * @return true when this provider supports subscriptions
     */
    boolean hasSubscriptions();


    /**
     * @return Current status
     */
    ProviderStatus getStatus();

    /**
     * Whether the node is connected or not
     *
     * @return true if connected
     */
    boolean isConnected();

    /**
     * Manually connect
     */
    CompletableFuture<Void> connect();


    /**
     * Manually disconnect from the connection, clearing auto-connect logic
     */
    CompletableFuture<Void> disconnect();

    /**
     * Subscribe to provider events
     *
     * @param type Event
     * @param sub  Callback
     * @return unsubscribe function
     */
    Runnable on(ProviderInterfaceEmitted type, EventListener sub);

    /**
     * Send data to the node
     *
     * @param method The RPC methods to execute
     * @param params Encoded parameters as applicable for the method
     * @return future containing result
     */
    CompletableFuture<Object> send(String method, // TODO replace `Object` to something like `JObject` to have more strict contract
                                   List<Object> params);

    /**
     * Send data to the node
     *
     * @param method The RPC methods to execute
     * @return future containing result
     */
    CompletableFuture<Object> send(String method);


    /**
     * Allows subscribing to a specific event
     *
     * @param type     Subscription type
     * @param method   The RPC methods to execute
     * @param params   Encoded parameters as applicable for the method
     * @param callback Callback
     * @return future containing subscription id
     */
    CompletableFuture<String> subscribe(String type,
                                        String method,
                                        List<Object> params,
                                        BiConsumer<Exception, Object> callback);

    /**
     * Allows unsubscribing to subscriptions made with {@link #subscribe(String, String, List, BiConsumer)}
     *
     * @param type   Subscription type
     * @param method The RPC methods to execute
     * @param id     Subscription id
     * @return true if unsubscribed
     */
    CompletableFuture<Boolean> unsubscribe(String type, String method, String id);
}
