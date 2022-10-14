package com.strategyobject.substrateclient.rpc.api.runtime;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import com.strategyobject.substrateclient.scale.readers.union.BaseUnionReader;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

@AutoRegister(types = XcmError.class)
public class XcmErrorReader extends BaseUnionReader<XcmError> {
    private final ScaleReaderRegistry registry;

    public XcmErrorReader(ScaleReaderRegistry registry) {
        super(26,
                x -> XcmError.ofOverflow(),
                x -> XcmError.ofUnimplemented(),
                x -> XcmError.ofUntrustedReserveLocation(),
                x -> XcmError.ofUntrustedTeleportLocation(),
                x -> XcmError.ofMultiLocationFull(),
                x -> XcmError.ofMultiLocationNotInvertible(),
                x -> XcmError.ofBadOrigin(),
                x -> XcmError.ofInvalidLocation(),
                x -> XcmError.ofAssetNotFound(),
                x -> XcmError.ofNotWithdrawable(),
                x -> XcmError.ofFailedToTransactAsset(),
                x -> XcmError.ofLocationCannotHold(),
                x -> XcmError.ofExceedsMaxMessageSize(),
                x -> XcmError.ofDestinationUnsupported(),
                x -> XcmError.ofTransport(),
                x -> XcmError.ofUnroutable(),
                x -> XcmError.ofUnknownClaim(),
                x -> XcmError.ofFailedToDecode(),
                x -> XcmError.ofMaxWeightInvalid(),
                x -> XcmError.ofNotHoldingFees(),
                x -> XcmError.ofTooExpensive(),
                x -> XcmError.ofTrap((BigInteger) x),
                x -> XcmError.ofUnhandledXcmVersion(),
                x -> XcmError.ofWeightLimitReached((BigInteger) x),
                x -> XcmError.ofBarrier(),
                x -> XcmError.ofWeightNotComputable());

        this.registry = registry;
    }






    @Override
    public XcmError read(@NonNull InputStream stream, ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers == null || readers.length == 0);

        val voidReader = registry.resolve(Void.class);
        val BigIntegerReader = registry.resolve(BigInteger.class);
        return super.read(stream,
                voidReader,
                voidReader,
                voidReader,
                voidReader,
                voidReader,
                voidReader,
                voidReader,
                voidReader,
                voidReader,
                voidReader,
                voidReader,
                voidReader,
                voidReader,
                voidReader,
                voidReader,
                voidReader,
                voidReader,
                voidReader,
                voidReader,
                voidReader,
                voidReader,
                BigIntegerReader,
                voidReader,
                BigIntegerReader,
                voidReader,
                voidReader);
    }
}
