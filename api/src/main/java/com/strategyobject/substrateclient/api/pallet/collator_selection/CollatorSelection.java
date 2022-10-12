package com.strategyobject.substrateclient.api.pallet.collator_selection;

import com.strategyobject.substrateclient.pallet.annotation.Event;
import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.rpc.api.primitives.Balance;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Pallet("CollatorSelection")
public interface CollatorSelection {
    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class NewInvulnerables {
        @Scale(ScaleType.Vec.class)
        private List<AccountId> invulnerables;
    }

    @Event(index = 1)
    @Getter
    @Setter
    @ScaleReader
    class NewDesiredCandidates {
        @Scale(ScaleType.U32.class)
        private Long desiredCandidates;
    }

    @Event(index = 2)
    @Getter
    @Setter
    @ScaleReader
    class NewCandidacyBond {
        private Balance bondAmount;
    }

    @Event(index = 3)
    @Getter
    @Setter
    @ScaleReader
    class CandidateAdded {
        private AccountId accountId;
        private Balance deposit;
    }

    @Event(index = 4)
    @Getter
    @Setter
    @ScaleReader
    class CandidateRemoved {
        private AccountId accountId;
    }
}
