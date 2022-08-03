package com.strategyobject.substrateclient.api.pallet.utility;

import com.strategyobject.substrateclient.pallet.annotation.Event;
import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.rpc.api.runtime.DispatchError;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

@Pallet("Utility")
public interface Utility {

    /**
     * Batch of dispatches did not complete fully. Index of first failing dispatch given, as
     * well as the error. \[index, error\]
     */
    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class BatchInterrupted {
        @Scale(ScaleType.U32.class)
        private Long index;
        private DispatchError dispatchError;
    }

    /**
     * Batch of dispatches completed fully with no error.
     */
    @Event(index = 1)
    @ScaleReader
    class BatchCompleted {
    }

    /**
     * A single item within a Batch of dispatches has completed with no error.
     */
    @Event(index = 2)
    @ScaleReader
    class ItemCompleted {
    }

}
