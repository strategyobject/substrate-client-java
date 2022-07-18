package com.strategyobject.substrateclient.transport.ws;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents a context why connection was closed
 *
 * @param <T> a type of policy's context required for
 *            computing the next delay or other policy's purposes
 */
@RequiredArgsConstructor(staticName = "of")
@Getter
public class ReconnectionContext<T> {
    /**
     * The code of the reason of disconnection
     */
    private final int code;

    /**
     * The text of the reason
     */
    private final String reason;

    /**
     * The policy's context
     */
    private final T policyContext;
}
