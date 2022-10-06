package com.strategyobject.substrateclient.api.pallet.transaction_payment;

import com.strategyobject.substrateclient.pallet.annotation.Event;
import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.rpc.api.primitives.Balance;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

@Pallet("TransactionPayment")
public interface TransactionPayment {
    /*
    pub enum Event<T: Config> {
    TransactionFeePaid {
        who: T::AccountId,
        actual_fee: <<T as Config>::OnChargeTransaction as OnChargeTransaction<T>>::Balance,
        tip: <<T as Config>::OnChargeTransaction as OnChargeTransaction<T>>::Balance,
    },
    // some variants omitted
}
     */
    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class TransactionFeePaid {
        private AccountId account;
        private Balance actualFee;
        private Balance tip;
    }
}
