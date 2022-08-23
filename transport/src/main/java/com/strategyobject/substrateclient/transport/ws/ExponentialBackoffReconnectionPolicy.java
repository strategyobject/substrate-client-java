package com.strategyobject.substrateclient.transport.ws;

import com.google.common.base.Preconditions;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * Represents an exponential backoff retry policy
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Slf4j
public class ExponentialBackoffReconnectionPolicy implements ReconnectionPolicy {
    /**
     * Max number of attempts
     */
    private final long maxAttempts;
    /**
     * Initial delay, the time to delay execution unit
     */
    private final long delay;
    /**
     * The time unit of the delay parameter
     */
    private final TimeUnit unit;
    /**
     * Max delay
     */
    private final long maxDelay;
    /**
     * A multiplier that's applied to delay after every attempt
     */
    private final double factor;

    /**
     * @param context contains a reason of disconnection and counter of attempts
     * @return a unit of time to delay the next reconnection
     */
    @Override
    public @NonNull Delay getNextDelay(@NonNull ReconnectionContext context) {
        val state = (LongAdder) context.getPolicyState();

        try {
            if (state.longValue() >= maxAttempts) {
                log.info("Provider won't reconnect more.");

                return Delay.NEVER;
            }

            var nextDelay = delay * Math.pow(factor, state.longValue());
            nextDelay = Math.min(nextDelay, maxDelay);

            log.info("Provider will try to reconnect after: {} {}", nextDelay, unit);
            return Delay.of((long) nextDelay, unit);
        } finally {
            state.increment();
        }
    }

    /**
     * Returns the counter of attempts
     */
    @Override
    public LongAdder initState() {
        return new LongAdder();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long delay = 15;
        private TimeUnit unit = TimeUnit.SECONDS;
        private long maxDelay = 150;
        private long maxAttempts = 10;
        private double factor = 2;

        Builder() {
        }

        public Builder retryAfter(long delay, TimeUnit unit) {
            Preconditions.checkArgument(delay >= 0);

            this.delay = delay;
            this.unit = unit;

            return this;
        }

        public Builder withFactor(double factor) {
            Preconditions.checkArgument(factor > 0);

            this.factor = factor;
            return this;
        }

        public Builder withMaxDelay(long maxDelay) {
            Preconditions.checkArgument(maxDelay >= 0);

            this.maxDelay = maxDelay;
            return this;
        }

        public Builder notMoreThan(long maxAttempts) {
            Preconditions.checkArgument(maxAttempts >= 0);

            this.maxAttempts = maxAttempts;
            return this;
        }

        public ExponentialBackoffReconnectionPolicy build() {
            return new ExponentialBackoffReconnectionPolicy(
                    this.maxAttempts,
                    this.delay,
                    this.unit,
                    this.maxDelay,
                    this.factor
            );
        }
    }
}
