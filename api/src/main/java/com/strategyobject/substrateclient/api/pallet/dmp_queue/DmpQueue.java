package com.strategyobject.substrateclient.api.pallet.dmp_queue;

import com.strategyobject.substrateclient.api.pallet.cumulus_xcm.Outcome;
import com.strategyobject.substrateclient.pallet.annotation.Event;
import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Pallet("DmpQueue")
public interface DmpQueue {

    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class InvalidFormat {
        private Byte[] messageId; //Array(size 32) u8 - type 1 - MessageId
    }

    @Event(index = 1)
    @Getter
    @Setter
    @ScaleReader
    class UnsupportedVersion {
        private Byte[] messageId; //Array(size 32) u8 - type 1 - MessageId
    }

    @Event(index = 2)
    @Getter
    @Setter
    @ScaleReader
    class ExecutedDownward {
        private Byte[] messageId; //Array(size 32) u8 - type 1 - MessageId

        private Outcome outcome;
    }

    @Event(index = 3)
    @Getter
    @Setter
    @ScaleReader
    class WeightExhausted {
        private Byte[] messageId; //Array(size 32) u8 - type 1 - MessageId

        @Scale(ScaleType.U64.class)
        private BigInteger remainingWeight;

        @Scale(ScaleType.U64.class)
        private BigInteger requiredWeight;
    }

    @Event(index = 4)
    @Getter
    @Setter
    @ScaleReader
    class OverweightEnqueued {
        private Byte[] messageId; //Array(size 32) u8 - type 1 - MessageId

        @Scale(ScaleType.U64.class)
        private BigInteger overweightIndex;

        @Scale(ScaleType.U64.class)
        private BigInteger requiredWeight;
    }

    @Event(index = 5)
    @Getter
    @Setter
    @ScaleReader
    class OverweightServiced {

        @Scale(ScaleType.U64.class)
        private BigInteger overweightIndex;

        @Scale(ScaleType.U64.class)
        private BigInteger weight_used;
    }
}