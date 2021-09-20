package com.strategyobject.substrateclient.common.streams;

import lombok.val;
import lombok.var;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class StreamUtils {

    public static int readByte(InputStream stream) throws IOException {
        val value = stream.read();
        if (value < 0) {
            throw new EOFException();
        }

        return value;
    }

    public static byte[] readBytes(int n, InputStream stream) throws IOException {
        return readBytes(n, stream, false);
    }

    public static byte[] readBytes(int n, InputStream stream, boolean reverse) throws IOException {
        val bytes = new byte[n];
        if (n <= 0) {
            return bytes;
        }

        val bytesRead = stream.read(bytes);
        if (bytesRead < n) {
            throw new EOFException();
        }

        if (reverse) {
            reverse(bytes);
        }

        return bytes;
    }

    public static void writeBytes(byte[] bytes, OutputStream stream) throws IOException {
        writeBytes(bytes, 0, stream, false);
    }

    public static void writeBytes(byte[] bytes,
                                  OutputStream stream,
                                  boolean reverse) throws IOException {
        writeBytes(bytes, 0, stream, reverse);
    }

    public static void writeBytes(byte[] bytes,
                                  int offset,
                                  OutputStream stream,
                                  boolean reverse) throws IOException {
        if (reverse) {
            reverse(bytes);
            stream.write(bytes, 0, bytes.length - offset);
        } else {
            stream.write(bytes, offset, bytes.length - offset);
        }
    }


    private static void reverse(byte[] bytes) {
        var lo = 0;
        var hi = bytes.length - 1;
        byte tmp;
        while (hi > lo) {
            tmp = bytes[hi];
            bytes[hi--] = bytes[lo];
            bytes[lo++] = tmp;
        }
    }

    private StreamUtils() {
    }
}
