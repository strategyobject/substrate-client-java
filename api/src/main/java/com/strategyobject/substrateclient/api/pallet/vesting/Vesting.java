package com.strategyobject.substrateclient.api.pallet.vesting;

import com.strategyobject.substrateclient.api.pallet.cumulus_xcm.Outcome;
import com.strategyobject.substrateclient.pallet.annotation.Event;
import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.rpc.api.primitives.Balance;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

@Pallet("Vesting")
public interface Vesting {

    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class VestingScheduleAdded {
        private AccountId from;
        private AccountId to;
        private VestingSchedule vestingSchedule;
    }

    @Event(index = 1)
    @Getter
    @Setter
    @ScaleReader
    class Claimed {
        private AccountId who;
        private Balance amount;
    }

    @Event(index = 2)
    @Getter
    @Setter
    @ScaleReader
    class VestingSchedulesUpdated {
        private AccountId who;
    }
}
