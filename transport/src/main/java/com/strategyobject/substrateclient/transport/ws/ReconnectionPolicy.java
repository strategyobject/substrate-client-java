package com.strategyobject.substrateclient.transport.ws;

import lombok.NonNull;

/**
 * Policy required for computing the next delay. Represents a strategy of reconnection.
 */
public interface ReconnectionPolicy {

    /**
     * The method is called when connection was closed and probably should be reconnected.
     *
     * @param context contains a reason of disconnection and policy's context.
     * @return a unit of time from now to delay reconnection.
     */
    @NonNull Delay getNextDelay(@NonNull ReconnectionContext context);

    /**
     * The method is called before the first connection or when the one successfully reestablished.
     *
     * @return a context required for the policy.
     */
    Object initState();

    /**
     * @return the builder of ExponentialBackoffReconnectionPolicy
     */
    static ExponentialBackoffReconnectionPolicy.Builder exponentialBackoff() {
        return ExponentialBackoffReconnectionPolicy.builder();
    }

    /**
     * The policy that's supposed to not reconnect automatically
     */
    ReconnectionPolicy MANUAL = new ReconnectionPolicy() {
        @Override
        public @NonNull Delay getNextDelay(@NonNull ReconnectionContext context) {
            return Delay.NEVER;
        }

        @Override
        public Void initState() {
            return null;
        }
    };
}
