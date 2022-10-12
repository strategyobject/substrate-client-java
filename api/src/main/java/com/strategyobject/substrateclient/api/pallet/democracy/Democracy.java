package com.strategyobject.substrateclient.api.pallet.democracy;

import com.strategyobject.substrateclient.pallet.annotation.*;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

@Pallet("Democracy")
public interface Democracy {
    /**
     * Value too low
     */
    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class ValueLow {}

    /**
     * Proposal does not exist
     */
    @Event(index = 1)
    @Getter
    @Setter
    @ScaleReader
    class ProposalMissing {}

    /**
     * Cannot cancel the same proposal twice
     */
    @Event(index = 2)
    @Getter
    @Setter
    @ScaleReader
    class AlreadyCanceled {}

    /**
     * Proposal already made
     */
    @Event(index = 3)
    @Getter
    @Setter
    @ScaleReader
    class DuplicateProposal {}

    /**
     * Proposal still blacklisted
     */
    @Event(index = 4)
    @Getter
    @Setter
    @ScaleReader
    class ProposalBlacklisted {}

    /**
     * Next external proposal not simple majority
     */
    @Event(index = 5)
    @Getter
    @Setter
    @ScaleReader
    class NotSimpleMajority {}

    /**
     * Invalid hash
     */
    @Event(index = 6)
    @Getter
    @Setter
    @ScaleReader
    class InvalidHash {}

    /**
     * No external proposal
     */
    @Event(index = 7)
    @Getter
    @Setter
    @ScaleReader
    class NoProposal {}

    /**
     * Identity may not veto a proposal twice
     */
    @Event(index = 8)
    @Getter
    @Setter
    @ScaleReader
    class AlreadyVetoed {}

    /**
     * Preimage already noted
     */
    @Event(index = 9)
    @Getter
    @Setter
    @ScaleReader
    class DuplicatePreimage {}

    /**
     * Not imminent
     */
    @Event(index = 10)
    @Getter
    @Setter
    @ScaleReader
    class NotImminent {}

    /**
     * Too early
     */
    @Event(index = 11)
    @Getter
    @Setter
    @ScaleReader
    class TooEarly {}

    /**
     * Imminent
     */
    @Event(index = 12)
    @Getter
    @Setter
    @ScaleReader
    class Imminent {}

    /**
     * Preimage not found
     */
    @Event(index = 13)
    @Getter
    @Setter
    @ScaleReader
    class PreimageMissing {}

    /**
     * Vote given for invalid referendum
     */
    @Event(index = 14)
    @Getter
    @Setter
    @ScaleReader
    class ReferendumInvalid {}

    /**
     * Invalid preimage
     */
    @Event(index = 15)
    @Getter
    @Setter
    @ScaleReader
    class PreimageInvalid {}

    /**
     * No proposals waiting
     */
    @Event(index = 16)
    @Getter
    @Setter
    @ScaleReader
    class NoneWaiting {}

    /**
     * The given account did not vote on the referendum.
     */
    @Event(index = 17)
    @Getter
    @Setter
    @ScaleReader
    class NotVoter {}

    /**
     * The actor has no permission to conduct the action.
     */
    @Event(index = 18)
    @Getter
    @Setter
    @ScaleReader
    class NoPermission {}

    /**
     * The account is already delegating.
     */
    @Event(index = 19)
    @Getter
    @Setter
    @ScaleReader
    class AlreadyDelegating {}

    /**
     * Too high a balance was provided that the account cannot afford.
     */
    @Event(index = 20)
    @Getter
    @Setter
    @ScaleReader
    class InsufficientFunds {}

    /**
     * The account is not currently delegating.
     */
    @Event(index = 21)
    @Getter
    @Setter
    @ScaleReader
    class NotDelegating {}

    /**
     * The account currently has votes attached to it and the operation cannot succeed until
     * these are removed, either through `unvote` or `reap_vote`.
     */
    @Event(index = 22)
    @Getter
    @Setter
    @ScaleReader
    class VotesExist {}

    /**
     * The instant referendum origin is currently disallowed.
     */
    @Event(index = 23)
    @Getter
    @Setter
    @ScaleReader
    class InstantNotAllowed {}

    /**
     * Delegation to oneself makes no sense.
     */
    @Event(index = 24)
    @Getter
    @Setter
    @ScaleReader
    class Nonsense {}

    /**
     * Invalid upper bound.
     */
    @Event(index = 25)
    @Getter
    @Setter
    @ScaleReader
    class WrongUpperBound {}

    /**
     * Maximum number of votes reached.
     */
    @Event(index = 26)
    @Getter
    @Setter
    @ScaleReader
    class MaxVotesReached {}

    /**
     * Maximum number of proposals reached.
     */
    @Event(index = 27)
    @Getter
    @Setter
    @ScaleReader
    class TooManyProposals {}

    /**
     * Voting period too low
     */
    @Event(index = 28)
    @Getter
    @Setter
    @ScaleReader
    class VotingPeriodLow {}
}
