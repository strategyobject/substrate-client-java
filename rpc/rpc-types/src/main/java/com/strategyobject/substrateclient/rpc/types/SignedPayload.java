package com.strategyobject.substrateclient.rpc.types;

import com.strategyobject.substrateclient.scale.ScaleWriterNotFoundException;
import com.strategyobject.substrateclient.types.Signable;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RequiredArgsConstructor
@Getter
public class SignedPayload<C extends Call, E extends Extra & SignedExtension>
    implements Signable {
    private final C call;
    private final E extra;

    @Override
    public byte @NonNull [] getBytes() {
        val buf = new ByteArrayOutputStream();
        try {
            call.write(buf);
            extra.write(buf);
            extra.getAdditionalExtra().write(buf);

            return buf.toByteArray();
        } catch (ScaleWriterNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
