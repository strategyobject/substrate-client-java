package com.strategyobject.substrateclient.api.pallet.democracy;

import com.strategyobject.substrateclient.rpc.api.primitives.Balance;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

public interface AccountVote {

    @Getter
    @Setter
    @ScaleReader
    class Standard{
        private Vote vote;
        private Balance balance;
    }

    @Getter
    @Setter
    @ScaleReader
    class Split{
        private Balance aye;
        private Balance nay;
    }
}