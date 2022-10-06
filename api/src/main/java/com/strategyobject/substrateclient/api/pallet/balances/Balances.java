package com.strategyobject.substrateclient.api.pallet.balances;

import com.strategyobject.substrateclient.pallet.annotation.Event;
import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.rpc.api.BalanceStatus;
import com.strategyobject.substrateclient.rpc.api.primitives.Balance;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

@Pallet("Balances")
public interface Balances {

    /**
     * An account was created with some free balance. \[account, free_balance\]
     */
    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class Endowed {
        private AccountId account;
        private Balance freeBalance;
    }

    /**
     * An account was removed whose balance was non-zero but below ExistentialDeposit,
     * resulting in an outright loss. \[account, balance\]
     */
    @Event(index = 1)
    @Getter
    @Setter
    @ScaleReader
    class DustLost {
        private AccountId account;
        private Balance balance;
    }

    /**
     * Transfer succeeded. \[from, to, value\]
     */
    @Event(index = 2)
    @Getter
    @Setter
    @ScaleReader
    class Transfer {
        private AccountId from;
        private AccountId to;
        private Balance value;
    }

    /**
     * A balance was set by root. \[who, free, reserved\]
     */
    @Event(index = 3)
    @Getter
    @Setter
    @ScaleReader
    class BalanceSet {
        private AccountId account;
        private Balance free;
        private Balance reserved;
    }

    /**
     * Some amount was deposited (e.g. for transaction fees). \[who, deposit\]
     */
    @Event(index = 7)
    @Getter
    @Setter
    @ScaleReader
    class Deposit {
        private AccountId account;
        private Balance value;
    }

    /**
     * Some balance was reserved (moved from free to reserved). \[who, value\]
     */
    @Event(index = 4)
    @Getter
    @Setter
    @ScaleReader
    class Reserved {
        private AccountId account;
        private Balance value;
    }

    /**
     * Some balance was unreserved (moved from reserved to free). \[who, value\]
     */
    @Event(index = 5)
    @Getter
    @Setter
    @ScaleReader
    class Unreserved {
        private AccountId account;
        private Balance value;
    }

    /**
     * Some balance was moved from the reserve of the first account to the second account.
     * Final argument indicates the destination balance type.
     * \[from, to, balance, destination_status\]
     */
    @Event(index = 6)
    @Getter
    @Setter
    @ScaleReader
    class ReserveRepatriated {
        private AccountId from;
        private AccountId to;
        private Balance value;
        private BalanceStatus destinationStatus;
    }

    @Event(index = 8)
    @Getter
    @Setter
    @ScaleReader
    class Withdraw {
        private AccountId account;
        private Balance value;
    }

    @Event(index = 9)
    @Getter
    @Setter
    @ScaleReader
    class Slashed {
        private AccountId account;
        private Balance value;
    }
}
