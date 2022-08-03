package com.strategyobject.substrateclient.api.pallet.sudo;

import com.strategyobject.substrateclient.common.types.Result;
import com.strategyobject.substrateclient.common.types.Unit;
import com.strategyobject.substrateclient.pallet.annotation.Event;
import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.rpc.api.runtime.DispatchError;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

@Pallet("Sudo")
public interface Sudo {

    /**
     * A sudo just took place. \[result\]
     */
    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class Sudid {
        private Result<Unit, DispatchError> dispatchResult;
    }

    /**
     * The \[sudoer\] just switched identity; the old key is supplied.
     */
    @Event(index = 1)
    @Getter
    @Setter
    @ScaleReader
    class KeyChanged {
        private AccountId sudoer;
    }

    /**
     * A sudo just took place. \[result\]
     */
    @Event(index = 2)
    @Getter
    @Setter
    @ScaleReader
    class SudoAsDone {
        private Result<Unit, DispatchError> dispatchResult;
    }

}
