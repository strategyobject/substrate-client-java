package com.strategyobject.substrateclient.api.pallet.staking;

import com.strategyobject.substrateclient.pallet.annotation.Event;
import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.rpc.api.primitives.Balance;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

@Pallet("Staking")
public interface Staking {

    /**
     * The era payout has been set; the first balance is the validator-payout; the second is
     * the remainder from the maximum amount of reward.
     * \[era_index, validator_payout, remainder\]
     */
    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class EraPaid {
        @Scale(ScaleType.U32.class)
        private Long eraIndex;
        private Balance payout;
        private Balance remainder;
    }

    /**
     * The nominator has been rewarded by this amount. \[stash, amount\]
     */
    @Event(index = 1)
    @Getter
    @Setter
    @ScaleReader
    class Rewarded {
        private AccountId stash;
        private Balance amount;
    }

    /**
     * One validator (and its nominators) has been slashed by the given amount.
     * \[validator, amount\]
     */
    @Event(index = 2)
    @Getter
    @Setter
    @ScaleReader
    class Slashed {
        private AccountId validator;
        private Balance amount;
    }

    /**
     * An old slashing report from a prior era was discarded because it could
     * not be processed. \[session_index\]
     */
    @Event(index = 3)
    @Getter
    @Setter
    @ScaleReader
    class OldSlashingReportDiscarded {
        @Scale(ScaleType.U32.class)
        private Long sessionIndex;
    }

    /**
     * A new set of stakers was elected.
     */
    @Event(index = 4)
    @ScaleReader
    class StakersElected {
    }

    /**
     * An account has bonded this amount. \[stash, amount\]
     * <p>
     * NOTE: This event is only emitted when funds are bonded via a dispatchable. Notably,
     * it will not be emitted for staking rewards when they are added to stake.
     */
    @Event(index = 5)
    @Getter
    @Setter
    @ScaleReader
    class Bonded {
        private AccountId stash;
        private Balance amount;
    }

    /**
     * An account has unbonded this amount. \[stash, amount\]
     */
    @Event(index = 6)
    @Getter
    @Setter
    @ScaleReader
    class Unbonded {
        private AccountId stash;
        private Balance amount;
    }

    /**
     * An account has called `withdraw_unbonded` and removed unbonding chunks worth `Balance`
     * from the unlocking queue. \[stash, amount\]
     */
    @Event(index = 7)
    @Getter
    @Setter
    @ScaleReader
    class Withdrawn {
        private AccountId stash;
        private Balance amount;
    }

    /**
     * A nominator has been kicked from a validator. \[nominator, stash\]
     */
    @Event(index = 8)
    @Getter
    @Setter
    @ScaleReader
    class Kicked {
        private AccountId nominator;
        private AccountId stash;
    }

    /**
     * The election failed. No new era is planned.
     */
    @Event(index = 9)
    @ScaleReader
    class StakingElectionFailed {
    }

    /**
     * An account has stopped participating as either a validator or nominator. \[stash\]
     */
    @Event(index = 10)
    @Getter
    @Setter
    @ScaleReader
    class Chilled {
        private AccountId stash;
    }

    /**
     * The stakers' rewards are getting paid. \[era_index, validator_stash\]
     */
    @Event(index = 11)
    @Getter
    @Setter
    @ScaleReader
    class PayoutStarted {
        @Scale(ScaleType.U32.class)
        private Long eraIndex;
        private AccountId validatorStash;
    }
}
