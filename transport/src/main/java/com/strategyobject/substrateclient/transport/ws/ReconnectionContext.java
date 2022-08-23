package com.strategyobject.substrateclient.transport.ws;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents a context why connection was closed
 */
@RequiredArgsConstructor(staticName = "of")
@Getter
public class ReconnectionContext {
    /**
     * The code of the reason of disconnection
     */
    private final int code;

    /**
     * The text of the reason
     */
    private final String reason;

    /**
     * The policy's state
     */
    private final Object policyState;
}
