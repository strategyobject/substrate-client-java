package com.strategyobject.substrateclient.api.pallet.polkadot_xcm;

import com.strategyobject.substrateclient.pallet.annotation.Event;
import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.rpc.api.primitives.Hash;
import com.strategyobject.substrateclient.rpc.api.primitives.Hash256;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Optional;

// THIS PALLET IS NONFUNCTIONAL
@Pallet("PolkadotXcm")
public interface PolkadotXcm {

    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class Attempted {
        @Scale(ScaleType.U32.class)
        private Long outcome; // u32 - type 4 - Outcome
    }

    @Event(index = 1)
    @Getter
    @Setter
    @ScaleReader
    class Sent {
        private MultiLocation multiLocation1;

        private MultiLocation multiLocation2;

        private Xcm xcm; // Empty Class
    }

    @Event(index = 2)
    @Getter
    @Setter
    @ScaleReader
    class UnexpectedResponse {
        private MultiLocation multiLocation;

        @Scale(ScaleType.U64.class)
        private BigInteger queryId; // u64 - type 8 - QueryId
    }

    @Event(index = 3)
    @Getter
    @Setter
    @ScaleReader
    class ResponseReady {
        @Scale(ScaleType.U64.class)
        private BigInteger queryId; // u64 - type 8 - QueryId

        private Response response; // Empty Class
    }

    @Event(index = 4)
    @Getter
    @Setter
    @ScaleReader
    class Notified {
        @Scale(ScaleType.U64.class)
        private BigInteger queryId; // u64 - type 8 - QueryId

        @Scale(ScaleType.U8.class)
        private Integer value1; // u8 - type 2 - u8

        @Scale(ScaleType.U8.class)
        private Integer value2; // u8 - type 2 - u8
    }

    @Event(index = 5)
    @Getter
    @Setter
    @ScaleReader
    class NotifyOverweight {
        @Scale(ScaleType.U64.class)
        private BigInteger queryId; // u64 - type 8 - QueryId

        @Scale(ScaleType.U8.class)
        private Integer value1; // u8 - type 2 - u8

        @Scale(ScaleType.U8.class)
        private Integer value2; // u8 - type 2 - u8

        @Scale(ScaleType.U64.class)
        private BigInteger weight1; // u64 - type 8 - Weight

        @Scale(ScaleType.U64.class)
        private BigInteger weight2; // u64 - type 8 - Weight
    }

    @Event(index = 6)
    @Getter
    @Setter
    @ScaleReader
    class NotifyDispatchError {
        @Scale(ScaleType.U64.class)
        private BigInteger queryId; // u64 - type 8 - QueryId

        @Scale(ScaleType.U8.class)
        private Integer value1; // u8 - type 2 - u8

        @Scale(ScaleType.U8.class)
        private Integer value2; // u8 - type 2 - u8
    }

    @Event(index = 7)
    @Getter
    @Setter
    @ScaleReader
    class NotifyDecodeFailed {
        @Scale(ScaleType.U64.class)
        private BigInteger queryId; // u64 - type 8 - QueryId

        @Scale(ScaleType.U8.class)
        private Integer value1; // u8 - type 2 - u8

        @Scale(ScaleType.U8.class)
        private Integer value2; // u8 - type 2 - u8
    }

    @Event(index = 8)
    @Getter
    @Setter
    @ScaleReader
    class InvalidResponder {
        private MultiLocation multiLocation;
        private BigInteger queryId; // u64 - type 8 - QueryId
        private Optional<MultiLocation> optionalMultiLocation;
    }

    @Event(index = 9)
    @Getter
    @Setter
    @ScaleReader
    class InvalidResponderVersion {
        private MultiLocation multiLocation;
        private BigInteger queryId; // u64 - type 8 - QueryId
    }

    @Event(index = 10)
    @Getter
    @Setter
    @ScaleReader
    class ResponseTaken {
        private BigInteger queryId; // u64 - type 8 - QueryId
    }

    @Event(index = 11)
    @Getter
    @Setter
    @ScaleReader
    class AssetsTrapped {
        private Hash256 h256; // Array(size 32) u8 - type 9 - H256
        private MultiLocation multiLocation;
        private VersionedMultiAssets versionedMultiAssets; // Empty Class
    }

    @Event(index = 12)
    @Getter
    @Setter
    @ScaleReader
    class VersionChangeNotified {
        private MultiLocation multiLocation;
        private BigInteger xcmVersion; //u32 - type 4 - XcmVersion
    }

    // TODO add rest of events if necessary
    // Stopped because polkadot events will hopefully not be needed for our purposes
}
