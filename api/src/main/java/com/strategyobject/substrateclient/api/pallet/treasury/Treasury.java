package com.strategyobject.substrateclient.api.pallet.treasury;

import com.strategyobject.substrateclient.pallet.annotation.Event;
import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.rpc.api.primitives.Balance;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;

import java.math.BigInteger;

@Pallet("Treasury")
public interface Treasury {

    /**
     * New proposal. \[proposal_index\]
     */
    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class Proposed {
        @Scale(ScaleType.U32.class)
        private Long proposalIndex;
    }

    /**
     * We have ended a spend period and will now allocate funds. \[budget_remaining\]
     */
    @Event(index = 1)
    @Getter
    @Setter
    @ScaleReader
    class Spending {
        private Balance budgetRemaining;
    }

    /**
     * Some funds have been allocated. \[proposal_index, award, beneficiary\]
     */
    @Event(index = 2)
    @Getter
    @Setter
    @ScaleReader
    class Awarded {
        @Scale(ScaleType.U32.class)
        private Long proposalIndex;
        private Balance award;
        private AccountId beneficiary;
    }

    /**
     * A proposal was rejected; funds were slashed. \[proposal_index, slashed\]
     */
    @Event(index = 3)
    @Getter
    @Setter
    @ScaleReader
    class Rejected {
        @Scale(ScaleType.U32.class)
        private Long proposalIndex;
        private Balance slashed;
    }

    /**
     * Some of our funds have been burnt. \[burn\]
     */
    @Event(index = 4)
    @Getter
    @Setter
    @ScaleReader
    class Burnt {
        private Balance value;
    }

    /**
     * Spending has finished; this is the amount that rolls over until next spend.
     * \[budget_remaining\]
     */
    @Event(index = 5)
    @Getter
    @Setter
    @ScaleReader
    class Rollover {
        private Balance budgetRemaining;
    }

    /**
     * Some funds have been deposited. \[deposit\]
     */
    @Event(index = 6)
    @Getter
    @Setter
    @ScaleReader
    class Deposit {
        private Balance value;
    }

    @Event(index = 7)
    @Getter
    @Setter
    @ScaleReader
    class SpendApproved {
        @Scale(ScaleType.U32.class)
        private Long proposalIndex;
        private Balance amount;
        private AccountId accountId;
    }

    @Event(index = 8)
    @Getter
    @Setter
    @ScaleReader
    class UpdatedInactive {
        private Balance reactivated;
        private Balance deactivated;
    }

}
