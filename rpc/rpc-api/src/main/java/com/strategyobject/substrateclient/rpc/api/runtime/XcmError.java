package com.strategyobject.substrateclient.rpc.api.runtime;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.types.union.Union;

import java.math.BigInteger;

/*
 * Reason why a XMC call failed.
 */
public class XcmError extends Union {

    private XcmError() {
    }

    public boolean isOverflow() {
        return index == 0;
    }
    public boolean isUnimplemented() {
        return index == 1;
    }
    public boolean isUntrustedReserveLocation() {
        return index == 2;
    }
    public boolean isUntrustedTeleportLocation() {
        return index == 3;
    }
    public boolean isMultiLocationFull() {
        return index == 4;
    }
    public boolean isMultiLocationNotInvertible() {
        return index == 5;
    }
    public boolean isBadOrigin() {
        return index == 6;
    }
    public boolean isInvalidLocation() {
        return index == 7;
    }
    public boolean isAssetNotFound() {
        return index == 8;
    }
    public boolean isNotWithdrawable() { return index == 10; }
    public boolean isFailedToTransactAsset() { return index == 9; }
    public boolean isLocationCannotHold() { return index == 11; }
    public boolean isExceedsMaxMessageSize() { return index == 12; }
    public boolean isDestinationUnsupported() { return index == 13; }
    public boolean isTransport() { return index == 14; }
    public boolean isUnroutable() { return index == 15; }
    public boolean isUnknownClaim() { return index == 16; }
    public boolean isFailedToDecode() { return index == 17; }
    public boolean isMaxWeightInvalid() { return index == 18; }
    public boolean isNotHoldingFees() { return index == 19; }
    public boolean isTooExpensive() { return index == 20; }
    public boolean isTrap() { return index == 21; } // field u64 type 8
    public boolean isUnhandledXcmVersion() { return index == 22; }
    public boolean isWeightLimitReached() { return index == 23; } // field weight (u64) type 8
    public boolean isBarrier() { return index == 24; }
    public boolean isWeightNotComputable() { return index == 25; }


    public BigInteger getTrap() {
        Preconditions.checkState(index == 21);
        return (BigInteger) value;
    }

    public BigInteger getWeightLimitReached() {
        Preconditions.checkState(index == 23);
        return (BigInteger) value;
    }

    public static XcmError ofOverflow() { return createXCMError(0, null); }
    public static XcmError ofUnimplemented() { return createXCMError(1, null); }
    public static XcmError ofUntrustedReserveLocation() { return createXCMError(2, null); }
    public static XcmError ofUntrustedTeleportLocation() { return createXCMError(3, null); }
    public static XcmError ofMultiLocationFull() { return createXCMError(4, null); }
    public static XcmError ofMultiLocationNotInvertible() { return createXCMError(5, null); }
    public static XcmError ofBadOrigin() { return createXCMError(6, null); }
    public static XcmError ofInvalidLocation() { return createXCMError(7, null); }
    public static XcmError ofAssetNotFound() { return createXCMError(8, null); }
    public static XcmError ofNotWithdrawable() { return createXCMError(10, null); }
    public static XcmError ofFailedToTransactAsset() { return createXCMError(9, null); }
    public static XcmError ofLocationCannotHold() { return createXCMError(11, null); }
    public static XcmError ofExceedsMaxMessageSize() { return createXCMError(12, null); }
    public static XcmError ofDestinationUnsupported() { return createXCMError(13, null); }
    public static XcmError ofTransport() { return createXCMError(14, null); }
    public static XcmError ofUnroutable() { return createXCMError(15, null); }
    public static XcmError ofUnknownClaim() { return createXCMError(16, null); }
    public static XcmError ofFailedToDecode() { return createXCMError(17, null); }
    public static XcmError ofMaxWeightInvalid() { return createXCMError(18, null); }
    public static XcmError ofNotHoldingFees() { return createXCMError(19, null); }
    public static XcmError ofTooExpensive() { return createXCMError(20, null); }
    public static XcmError ofTrap(BigInteger u64) { return createXCMError(21, u64); } // field u64 type 8
    public static XcmError ofUnhandledXcmVersion() { return createXCMError(22, null); }
    public static XcmError ofWeightLimitReached(BigInteger weight) { return createXCMError(23, weight); } // field weight (u64) type 8
    public static XcmError ofBarrier() { return createXCMError(24, null); }
    public static XcmError ofWeightNotComputable() { return createXCMError(25, null); }

    public static XcmError createXCMError(int index, Object value) {
        XcmError error = new XcmError();
        if(value != null) error.value = value;
        error.index = index;
        return error;
    }
}