package com.strategyobject.substrateclient.api.pallet.multisig;

import com.strategyobject.substrateclient.common.types.Result;
import com.strategyobject.substrateclient.common.types.Unit;
import com.strategyobject.substrateclient.pallet.annotation.Event;
import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.rpc.api.primitives.CallHash;
import com.strategyobject.substrateclient.rpc.api.runtime.DispatchError;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

@Pallet("Multisig")
public interface Multisig {

    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class NewMultisig {
        private AccountId approving;
        private AccountId multisig;
        private CallHash callHash;
    }

    @Event(index = 1)
    @Getter
    @Setter
    @ScaleReader
    class MultisigApproval {
        private AccountId approving;
        private Timepoint timepoint;
        private AccountId multisig;
        private CallHash callHash;
    }

    @Event(index = 2)
    @Getter
    @Setter
    @ScaleReader
    class MultisigExecuted {
        private AccountId approving;
        private Timepoint timepoint;
        private AccountId multisig;
        private CallHash callHash;
        private Result<Unit, DispatchError> result;
    }

    @Event(index = 3)
    @Getter
    @Setter
    @ScaleReader
    class MultisigCancelled {
        private AccountId cancelling;
        private Timepoint timepoint;
        private AccountId multisig;
        private CallHash callHash;
    }
}