package com.strategyobject.substrateclient.pallet.events;

import com.strategyobject.substrateclient.rpc.metadata.Pallet;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * The event descriptor holding event description data and the event itself.
 */
@Getter
@RequiredArgsConstructor
public class EventDescriptor {
    /**
     * Reference to the Pallet that produced the event.
     */
    private final Pallet pallet;

    /**
     * Index of the event.
     */
    private final int eventIndex;

    /**
     * The event itself.
     */
    private final @NonNull Object event;
}
