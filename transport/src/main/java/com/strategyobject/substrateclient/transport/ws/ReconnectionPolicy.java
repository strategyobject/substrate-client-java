package com.strategyobject.substrateclient.transport.ws;

import lombok.NonNull;

/**
 * @param <T> a type of policy's context required for
 *            computing the next delay or other policy's purposes
 *            Represents a strategy of reconnection
 */
public interface ReconnectionPolicy<T> {

    /**
     * The method is called when connection was closed and probably should be reconnected.
     * @param context contains a reason of disconnection and policy's context.
     * @return a unit of time from now to delay reconnection.
     */
    @NonNull Delay getNextDelay(@NonNull ReconnectionContext<T> context);

    /**
     * The method is called before the first connection or when the one successfully reestablished.
     * @return a context required for the policy.
     */
    T initContext();

    /**
     * @return the builder of ExponentialBackoffReconnectionPolicy
     */
    static ExponentialBackoffReconnectionPolicy.Builder exponentialBackoff() {
        return ExponentialBackoffReconnectionPolicy.builder();
    }

    /**
     * @param <T> the type of context
     * @return the policy that's supposed to not reconnect automatically
     */
    @SuppressWarnings("unchecked")
    static <T> ReconnectionPolicy<T> manual() {
        return (ReconnectionPolicy<T>) MANUAL;
    }

    /**
     * The policy that's supposed to not reconnect automatically
     */
    ReconnectionPolicy<?> MANUAL = new ReconnectionPolicy<Void>() {
        @Override
        public @NonNull Delay getNextDelay(@NonNull ReconnectionContext<Void> context) {
            return Delay.NEVER;
        }

        @Override
        public Void initContext() {
            return null;
        }
    };
}
