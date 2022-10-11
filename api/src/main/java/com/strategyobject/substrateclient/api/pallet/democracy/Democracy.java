package com.strategyobject.substrateclient.api.pallet.democracy;

import com.strategyobject.substrateclient.pallet.annotation.*;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

@Pallet("Democracy")
public interface Democracy {
    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class ValueLow {}

    @Event(index = 1)
    @Getter
    @Setter
    @ScaleReader
    class ProposalMissing {}

    @Event(index = 2)
    @Getter
    @Setter
    @ScaleReader
    class AlreadyCanceled {}

    @Event(index = 3)
    @Getter
    @Setter
    @ScaleReader
    class DuplicateProposal {}

    @Event(index = 4)
    @Getter
    @Setter
    @ScaleReader
    class ProposalBlacklisted {}

    @Event(index = 5)
    @Getter
    @Setter
    @ScaleReader
    class NotSimpleMajority {}

    @Event(index = 6)
    @Getter
    @Setter
    @ScaleReader
    class InvalidHash {}

    @Event(index = 7)
    @Getter
    @Setter
    @ScaleReader
    class NoProposal {}

    @Event(index = 8)
    @Getter
    @Setter
    @ScaleReader
    class AlreadyVetoed {}

    @Event(index = 9)
    @Getter
    @Setter
    @ScaleReader
    class DuplicatePreimage {}

    @Event(index = 10)
    @Getter
    @Setter
    @ScaleReader
    class NotImminent {}

    @Event(index = 11)
    @Getter
    @Setter
    @ScaleReader
    class TooEarly {}

    @Event(index = 12)
    @Getter
    @Setter
    @ScaleReader
    class Imminent {}

    @Event(index = 13)
    @Getter
    @Setter
    @ScaleReader
    class PreimageMissing {}

    @Event(index = 14)
    @Getter
    @Setter
    @ScaleReader
    class ReferendumInvalid {}

    @Event(index = 15)
    @Getter
    @Setter
    @ScaleReader
    class PreimageInvalid {}

    @Event(index = 16)
    @Getter
    @Setter
    @ScaleReader
    class NoneWaiting {}

    @Event(index = 17)
    @Getter
    @Setter
    @ScaleReader
    class NotVoter {}

    @Event(index = 18)
    @Getter
    @Setter
    @ScaleReader
    class NoPermission {}

    @Event(index = 19)
    @Getter
    @Setter
    @ScaleReader
    class AlreadyDelegating {}

    @Event(index = 20)
    @Getter
    @Setter
    @ScaleReader
    class InsufficientFunds {}

    @Event(index = 21)
    @Getter
    @Setter
    @ScaleReader
    class NotDelegating {}

    @Event(index = 22)
    @Getter
    @Setter
    @ScaleReader
    class VotesExist {}

    @Event(index = 23)
    @Getter
    @Setter
    @ScaleReader
    class InstantNotAllowed {}

    @Event(index = 24)
    @Getter
    @Setter
    @ScaleReader
    class Nonsense {}

    @Event(index = 25)
    @Getter
    @Setter
    @ScaleReader
    class WrongUpperBound {}

    @Event(index = 26)
    @Getter
    @Setter
    @ScaleReader
    class MaxVotesReached {}

    @Event(index = 27)
    @Getter
    @Setter
    @ScaleReader
    class TooManyProposals {}

    @Event(index = 28)
    @Getter
    @Setter
    @ScaleReader
    class VotingPeriodLow {}
}
