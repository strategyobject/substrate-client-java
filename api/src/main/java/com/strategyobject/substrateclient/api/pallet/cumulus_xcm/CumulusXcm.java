package com.strategyobject.substrateclient.api.pallet.cumulus_xcm;

import com.strategyobject.substrateclient.pallet.annotation.Event;
import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.rpc.api.primitives.Hash;
import com.strategyobject.substrateclient.rpc.api.primitives.Hash256;
import com.strategyobject.substrateclient.rpc.api.runtime.XcmError;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Optional;

@Pallet("CumulusXcm")
public interface CumulusXcm {

    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class InvalidFormat {
        private Byte[] array; //Array(size 8) u8 - type 77
    }

    @Event(index = 1)
    @Getter
    @Setter
    @ScaleReader
    class UnsupportedVersion {
        private Byte[] array; //Array(size 8) u8 - type 77
    }

    @Event(index = 2)
    @Getter
    @Setter
    @ScaleReader
    class ExecutedDownward {
        private Byte[] array; //Array(size 8) u8 - type 77
        private Outcome outcome;
    }
}
