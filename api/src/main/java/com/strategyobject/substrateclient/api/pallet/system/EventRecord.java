package com.strategyobject.substrateclient.api.pallet.system;

import com.strategyobject.substrateclient.pallet.events.EventDescriptor;
import com.strategyobject.substrateclient.rpc.api.primitives.Hash;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Record of an event happening.
 */
@Getter
@Setter
@ScaleReader
public class EventRecord {
    /**
     * Record of an event happening.
     */
    private Phase phase;
    /**
     * The event descriptor holding event description data and the event itself.
     */
    private EventDescriptor event;
    /**
     * The list of the topics this event has.
     */
    private List<Hash> topics;
}
