package com.strategyobject.substrateclient.api.pallet.indices;

import com.strategyobject.substrateclient.pallet.annotation.Event;
import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.rpc.api.primitives.AccountIndex;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

@Pallet("Indices")
public interface Indices {

    /**
     * An account index was assigned. \[index, who\]
     */
    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class IndexAssigned {
        private AccountId account;
        private AccountIndex index;
    }

    /**
     * An account index has been freed up (unassigned). \[index\]
     */
    @Event(index = 1)
    @Getter
    @Setter
    @ScaleReader
    class IndexFreed {
        private AccountIndex accountIndex;
    }

    /**
     * An account index has been frozen to its current account ID. \[index, who\]
     */
    @Event(index = 2)
    @Getter
    @Setter
    @ScaleReader
    class IndexFrozen {
        private AccountIndex index;
        private AccountId account;
    }
}
