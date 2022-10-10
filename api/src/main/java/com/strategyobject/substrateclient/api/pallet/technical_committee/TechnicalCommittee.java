package com.strategyobject.substrateclient.api.pallet.technical_committee;

import com.strategyobject.substrateclient.common.types.Result;
import com.strategyobject.substrateclient.common.types.Unit;
import com.strategyobject.substrateclient.pallet.annotation.Event;
import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.rpc.api.primitives.Hash;
import com.strategyobject.substrateclient.rpc.api.runtime.DispatchError;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

@Pallet("TechnicalCommittee")
public interface TechnicalCommittee {
    /**
     * A motion (given hash) has been proposed (by given account) with a threshold (given MemberCount).
     */
    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class Proposed {
        private AccountId account;
        @Scale(ScaleType.U32.class)
        private Long proposalIndex;
        private Hash proposalHash;
        @Scale(ScaleType.U32.class)
        private Long threshold;
    }

    /**
     *  A motion (given hash) has been voted on by given account, leaving
     *  a tally (yes votes and no votes given respectively as `MemberCount`).
     */
    @Event(index = 1)
    @Getter
    @Setter
    @ScaleReader
    class Voted {
        private AccountId account;
        private Hash proposalHash;
        private Boolean voted;
        @Scale(ScaleType.U32.class)
        private Long votedYes;
        @Scale(ScaleType.U32.class)
        private Long votedNo;
    }

    /**
     * A motion was approved by the required threshold.
     */
    @Event(index = 2)
    @Getter
    @Setter
    @ScaleReader
    class Approved {
        private Hash proposalHash;
    }

    /**
     * A motion was not approved by the required threshold.
     */
    @Event(index = 3)
    @Getter
    @Setter
    @ScaleReader
    class Disapproved {
        private Hash proposalHash;
    }

    /**
     * A motion was executed; result will be `Ok` if it returned without error.
     */
    @Event(index = 4)
    @Getter
    @Setter
    @ScaleReader
    class Executed {
        private Hash proposalHash;
        private Result<Unit, DispatchError> result;
    }

    /**
     * A single member did some action; result will be `Ok` if it returned without error.
     */
    @Event(index = 5)
    @Getter
    @Setter
    @ScaleReader
    class MemberExecuted {
        private Hash proposalHash;
        private Result<Unit, DispatchError> result;
    }

    /**
     * A proposal was closed because its threshold was reached or after its duration was up.
     */
    @Event(index = 6)
    @Getter
    @Setter
    @ScaleReader
    class Closed {
        private Hash proposalHash;
        @Scale(ScaleType.U32.class)
        private Long votedYesCount;
        @Scale(ScaleType.U32.class)
        private Long votedNoCount;
    }
}
