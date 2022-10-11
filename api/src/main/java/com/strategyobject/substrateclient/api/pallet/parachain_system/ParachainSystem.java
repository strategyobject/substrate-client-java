package com.strategyobject.substrateclient.api.pallet.parachain_system;

import com.strategyobject.substrateclient.pallet.annotation.*;
import com.strategyobject.substrateclient.rpc.api.primitives.Hash;
import com.strategyobject.substrateclient.rpc.api.primitives.Hash256;
import com.strategyobject.substrateclient.rpc.api.weights.DispatchInfo;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;


@Pallet("ParachainSystem")
public interface ParachainSystem {
    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class ValidationFunctionStored {}

    @Event(index = 1)
    @Getter
    @Setter
    @ScaleReader
    class ValidationFunctionApplied {
        @Scale(ScaleType.U32.class)
        private Long relayChainBlockNum;
    }

    @Event(index = 2)
    @Getter
    @Setter
    @ScaleReader
    class ValidationFunctionDiscarded {}

    @Event(index = 3)
    @Getter
    @Setter
    @ScaleReader
    class UpgradeAuthorized {
        private Hash codeHash; //H256
    }

    @Event(index = 4)
    @Getter
    @Setter
    @ScaleReader
    class DownwardMessagesReceived {
        @Scale(ScaleType.U32.class)
        private Long count;
    }

    @Event(index = 5)
    @Getter
    @Setter
    @ScaleReader
    class DownwardMessagesProcessed {
        @Scale(ScaleType.U64.class)
        private BigInteger weightUsed;
        private Hash dmqHead;
    }
}
