package com.strategyobject.substrateclient.transport.ws;

import lombok.val;
import lombok.var;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExponentialBackoffReconnectionPolicyTest {
    @ParameterizedTest
    @CsvSource({
            "-1,10,20,SECONDS,5,1.5,7",
            "10,5,10,SECONDS,10,2,10",
            "20,5,100,SECONDS,10,2,3",
            "10,10,10,MINUTES,10,2,10",
            "5,100,5,MILLISECONDS,10,2,10",
            "25,100,100,MILLISECONDS,3,0.5,3"})
    void getNextDelay(long expected,
                      long initialDelay,
                      long maxDelay,
                      TimeUnit unit,
                      int maxAttempts,
                      double factor,
                      int iterations) {
        val policy = ExponentialBackoffReconnectionPolicy.builder()
                .retryAfter(initialDelay, unit)
                .withFactor(factor)
                .withMaxDelay(maxDelay)
                .notMoreThan(maxAttempts)
                .build();
        val context = policy.initContext();

        for (var i = 0; i < iterations - 1; i++) {
            policy.getNextDelay(ReconnectionContext.of(-1, "some", context));
        }

        val delay = policy.getNextDelay(ReconnectionContext.of(-1, "some", context));

        assertEquals(iterations, context.intValue());
        assertEquals(expected, delay.getValue());
    }
}