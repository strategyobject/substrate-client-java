package com.strategyobject.substrateclient.api.pallet.cumulus_xcm;

import com.strategyobject.substrateclient.rpc.api.runtime.XcmError;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

/*
 * the Outcome of the Cumulus ExecutedDownward event.
 */
public interface Outcome {

    @Getter
    @Setter
    @ScaleReader
    class Complete{
        @Scale(ScaleType.U64.class)
        private BigInteger weight;

    }

    @Getter
    @Setter
    @ScaleReader
    class Incomplete{
        @Scale(ScaleType.U64.class)
        private BigInteger weight;
        private XcmError error;
    }

    @Getter
    @Setter
    @ScaleReader
    class Error {
        private XcmError error;
    }


}