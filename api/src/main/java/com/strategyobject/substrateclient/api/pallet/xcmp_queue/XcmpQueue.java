package com.strategyobject.substrateclient.api.pallet.xcmp_queue;

import com.strategyobject.substrateclient.pallet.annotation.Event;
import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.rpc.api.primitives.Hash;
import com.strategyobject.substrateclient.rpc.api.runtime.XcmError;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleGeneric;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Optional;

@Pallet("XcmpQueue")
public interface XcmpQueue {

    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class Success {
        @Scale(ScaleType.Option.class)
        private Optional<Hash> messageHash;

        @Scale(ScaleType.U64.class)
        private BigInteger weight; // u64 - type 8 - Weight
    }

    @Event(index = 1)
    @Getter
    @Setter
    @ScaleReader
    class Fail {
        @Scale(ScaleType.Option.class)
        private Optional<Hash> messageHash;

        private XcmError error;

        @Scale(ScaleType.U64.class)
        private BigInteger weight; // u64 - type 8 - Weight
    }

    @Event(index = 2)
    @Getter
    @Setter
    @ScaleReader
    class BadVersion {
        @Scale(ScaleType.Option.class)
        private Optional<Hash> messageHash;
    }

    @Event(index = 3)
    @Getter
    @Setter
    @ScaleReader
    class BadFormat {
        @Scale(ScaleType.Option.class)
        private Optional<Hash> messageHash;
    }

    @Event(index = 4)
    @Getter
    @Setter
    @ScaleReader
    class UpwardMessageSent {
        @Scale(ScaleType.Option.class)
        private Optional<Hash> messageHash;
    }

    @Event(index = 5)
    @Getter
    @Setter
    @ScaleReader
    class XcmpMessageSent {
        @Scale(ScaleType.Option.class)
        private Optional<Hash> messageHash;
    }

    @Event(index = 6)
    @Getter
    @Setter
    @ScaleReader
    class OverweightEnqueued {
        @Scale(ScaleType.U32.class)
        private Long sender; // u32 - type 54 - ParaId

        @Scale(ScaleType.U32.class)
        private Long sentAt; //u32 - type 4 - RelayBlockNumber

        @Scale(ScaleType.U64.class)
        private BigInteger index; // u64 - type 8 - OverweightIndex

        @Scale(ScaleType.U64.class)
        private BigInteger required; // u64 - type 8 - Weight
    }

    @Event(index = 7)
    @Getter
    @Setter
    @ScaleReader
    class OverweightServiced {
        @Scale(ScaleType.U64.class)
        private BigInteger index; // u64 - type 8 - OverweightIndex

        @Scale(ScaleType.U64.class)
        private BigInteger used; // u64 - type 8 - Weight
    }
}
