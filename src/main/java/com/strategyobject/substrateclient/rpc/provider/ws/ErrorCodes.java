package com.strategyobject.substrateclient.rpc.provider.ws;

import com.google.common.base.MoreObjects;

import java.util.HashMap;
import java.util.Map;

public final class ErrorCodes {
    private static final Map<Integer, String> KNOWN = new HashMap<>();

    static {
        KNOWN.put(1000, "Normal Closure");
        KNOWN.put(1001, "Going Away");
        KNOWN.put(1002, "Protocol Error");
        KNOWN.put(1003, "Unsupported Data");
        KNOWN.put(1004, "(For future)");
        KNOWN.put(1005, "No Status Received");
        KNOWN.put(1006, "Abnormal Closure");
        KNOWN.put(1007, "Invalid frame payload data");
        KNOWN.put(1008, "Policy Violation");
        KNOWN.put(1009, "Message too big");
        KNOWN.put(1010, "Missing Extension");
        KNOWN.put(1011, "Internal Error");
        KNOWN.put(1012, "Service Restart");
        KNOWN.put(1013, "Try Again Later");
        KNOWN.put(1014, "Bad Gateway");
        KNOWN.put(1015, "TLS Handshake");
    }

    private static String getUnmapped(int code) {
        if (code <= 1999) {
            return "(For WebSocket standard)";
        } else if (code <= 2999) {
            return "(For WebSocket extensions)";
        } else if (code <= 3999) {
            return "(For libraries and frameworks)";
        } else if (code <= 4999) {
            return "(For applications)";
        }

        return null;
    }

    public static String getWSErrorString(int code) {
        if (code >= 0 && code <= 999) {
            return "(Unused)";
        }

        return KNOWN.getOrDefault(code, MoreObjects.firstNonNull(getUnmapped(code), "(Unknown)"));
    }

    private ErrorCodes() {
    }
}
