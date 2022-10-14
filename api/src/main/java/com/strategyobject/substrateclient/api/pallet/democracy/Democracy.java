package com.strategyobject.substrateclient.api.pallet.democracy;

import com.strategyobject.substrateclient.common.types.Result;
import com.strategyobject.substrateclient.common.types.Unit;
import com.strategyobject.substrateclient.pallet.annotation.*;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.rpc.api.primitives.Balance;
import com.strategyobject.substrateclient.rpc.api.primitives.Hash;
import com.strategyobject.substrateclient.rpc.api.runtime.DispatchError;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleGeneric;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Pallet("Democracy")
public interface Democracy {
    /**
     * A motion has been proposed by a public account.
     */
    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class Proposed {
        @Scale(ScaleType.U32.class)
        private Long proposalIndex;
        private Balance deposit;
    }

    /**
     * A public proposal has been tabled for referendum vote.
     */
    @Event(index = 1)
    @Getter
    @Setter
    @ScaleReader
    class Tabled {
        @Scale(ScaleType.U32.class)
        private Long proposalIndex;
        private Balance deposit;
        @ScaleGeneric(
                template = "Vec<AccountId>",
                types = {
                        @Scale(ScaleType.Vec.class),
                })
        private List<AccountId> depositors;
    }

    /**
     * An external proposal has been tabled.
     */
    @Event(index = 2)
    @ScaleReader
    class ExternalTabled {}

    /**
     * A referendum has begun.
     */
    @Event(index = 3)
    @Getter
    @Setter
    @ScaleReader
    class Started {
        @Scale(ScaleType.U32.class)
        private Long refIndex;
        private VoteThreshold threshold;
    }

    /**
     * A proposal has been approved by referendum.
     */
    @Event(index = 4)
    @Getter
    @Setter
    @ScaleReader
    class Passed {
        @Scale(ScaleType.U32.class)
        private Long refIndex;
    }

    /**
     * A proposal has been rejected by referendum.
     */
    @Event(index = 5)
    @Getter
    @Setter
    @ScaleReader
    class NotPassed {
        @Scale(ScaleType.U32.class)
        private Long refIndex;
    }

    /**
     * A referendum has been cancelled.
     */
    @Event(index = 6)
    @Getter
    @Setter
    @ScaleReader
    class Cancelled {
        @Scale(ScaleType.U32.class)
        private Long refIndex;
    }

    /**
     * A proposal has been enacted.
     */
    @Event(index = 7)
    @Getter
    @Setter
    @ScaleReader
    class Executed {
        @Scale(ScaleType.U32.class)
        private Long refIndex;
        private Result<Unit, DispatchError> result;
    }

    /**
     * An account has delegated their vote to another account.
     */
    @Event(index = 8)
    @Getter
    @Setter
    @ScaleReader
    class Delegated {
        private AccountId who;
        private AccountId target;
    }

    /**
     * An account has cancelled a previous delegation operation.
     */
    @Event(index = 9)
    @Getter
    @Setter
    @ScaleReader
    class Undelegated {
        private AccountId account;
    }

    /**
     * An external proposal has been vetoed.
     */
    @Event(index = 10)
    @Getter
    @Setter
    @ScaleReader
    class Vetoed {
        private AccountId who;
        private Hash proposalHash;
        @Scale(ScaleType.U32.class)
        private Long until;
    }

    /**
     * A proposal's preimage was noted, and the deposit taken.
     */
    @Event(index = 11)
    @Getter
    @Setter
    @ScaleReader
    class PreimageNoted {
        private Hash proposalHash;
        private AccountId who;
        private Balance deposit;
    }

    /**
     * A proposal preimage was removed and used (the deposit was returned).
     */
    @Event(index = 12)
    @Getter
    @Setter
    @ScaleReader
    class PreimageUsed {
        private Hash proposalHash;
        private AccountId provider;
        private Balance deposit;
    }

    /**
     * A proposal could not be executed because its preimage was invalid.
     */
    @Event(index = 13)
    @Getter
    @Setter
    @ScaleReader
    class PreimageInvalid {
        private Hash proposalHash;
        @Scale(ScaleType.U32.class)
        private Long refIndex;
    }

    /**
     * A proposal could not be executed because its preimage was missing.
     */
    @Event(index = 14)
    @Getter
    @Setter
    @ScaleReader
    class PreimageMissing {
        private Hash proposalHash;
        @Scale(ScaleType.U32.class)
        private Long refIndex;
    }

    /**
     * A registered preimage was removed and the deposit collected by the reaper.
     */
    @Event(index = 15)
    @Getter
    @Setter
    @ScaleReader
    class PreimageReaped {
        private Hash proposalHash;
        private AccountId provider;
        private Balance deposit;
        private AccountId reaper;
    }

    /**
     * A proposal_hash has been blacklisted permanently.
     */
    @Event(index = 16)
    @Getter
    @Setter
    @ScaleReader
    class Blacklisted {
        private Hash proposalHash;
    }

    /**
     * An account has voted in a referendum
     */
    @Event(index = 17)
    @Getter
    @Setter
    @ScaleReader
    class Voted {
        private AccountId voter;
        @Scale(ScaleType.U32.class)
        private Long refIndex;
        private AccountVote vote;

    }

    /**
     * An account has seconded a proposal
     */
    @Event(index = 18)
    @Getter
    @Setter
    @ScaleReader
    class Seconded {
        private AccountId seconder;
        @Scale(ScaleType.U32.class)
        private Long propIndex;
    }

    /**
     * A proposal got canceled.
     */
    @Event(index = 19)
    @Getter
    @Setter
    @ScaleReader
    class ProposalCanceled {
        @Scale(ScaleType.U32.class)
        private Long propIndex;
    }
}
