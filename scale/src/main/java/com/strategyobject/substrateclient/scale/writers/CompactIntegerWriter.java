package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.scale.CompactMode;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.val;
import lombok.var;

import java.io.IOException;
import java.io.OutputStream;

public class CompactIntegerWriter implements ScaleWriter<Integer> {
    @Override
    public void write(Integer value, OutputStream stream) throws IOException {
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
