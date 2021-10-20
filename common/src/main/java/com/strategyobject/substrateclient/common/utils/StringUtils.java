package com.strategyobject.substrateclient.common.utils;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.val;
import lombok.var;

import java.util.ArrayList;
import java.util.List;

public final class StringUtils {
    private static int indexOfAny(String target, String chars, int fromIndex) {
        if (fromIndex < 0) {
            fromIndex = 0;
        } else if (fromIndex >= target.length()) {
            return -1;
        }

        for (var i = fromIndex; i < target.length(); i++) {
            if (chars.indexOf(target.charAt(i)) != -1) {
                return i;
            }
        }

        return -1;
    }

    public static List<Integer> allIndexesOfAny(@NonNull String target, @NonNull String chars) {
        Preconditions.checkArgument(!chars.isEmpty());

        val result = new ArrayList<Integer>();
        var index = indexOfAny(target, chars, 0);
        while (index >= 0) {
            result.add(index);
            index = indexOfAny(target, chars, index + 1);
        }

        return result;
    }

    public static String capitalize(@NonNull String source) {
        return source.substring(0, 1).toUpperCase() + source.substring(1);
    }

    private StringUtils() {
    }
}
