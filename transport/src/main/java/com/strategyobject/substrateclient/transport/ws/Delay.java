package com.strategyobject.substrateclient.transport.ws;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * Represents a delay
 */
@RequiredArgsConstructor(staticName = "of")
@Getter
public class Delay {
    /**
     * The time to delay execution unit
     */
    private final long value;

    /**
     * The time unit of the delay parameter
     */
    private final TimeUnit unit;

    /**
     * A delay that should never be scheduled
     */
    public static final Delay NEVER = Delay.of(-1, TimeUnit.SECONDS);
}
