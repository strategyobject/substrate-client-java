package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.CompactMode;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;
import lombok.val;
import lombok.var;

import java.io.IOException;
import java.io.OutputStream;

public class CompactIntegerWriter implements ScaleWriter<Integer> {
    @Override
    public void write(@NonNull Integer value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);

        writeInternal(value, stream);
    }

    static void writeInternal(Integer value, OutputStream stream) throws IOException {
        val mode = CompactMode.fromNumber(value);
        writeValue(value, stream, mode);
    }

    static void writeValue(Integer value, OutputStream stream, CompactMode mode) throws IOException {
        var compact = (value << 2) + mode.getValue();
        switch (mode) {
            case SINGLE:
                stream.write(compact & 0xff);
                break;
            case TWO:
                stream.write(compact & 0xff);
                stream.write((compact >> 8) & 0xff);
                break;
            case FOUR:
                stream.write(compact & 0xff);
                stream.write((compact >> 8) & 0xff);
                stream.write((compact >> 8 * 2) & 0xff);
                stream.write((compact >> 8 * 3) & 0xff);
                break;
            default:
                throw new UnsupportedOperationException("The number is in Big-integer mode.");
        }
    }
}
