package com.strategyobject.substrateclient.scale.codegen;

import com.strategyobject.substrateclient.scale.ScaleDispatch;
import com.strategyobject.substrateclient.scale.annotation.Default;

public class ScaleProcessorHelper {
    public static final Class<?> SCALE_DISPATCH = ScaleDispatch.class;
    public static final Class<?> SCALE_ANNOTATIONS_DEFAULT = Default.class;
    private static final String READER_NAME_TEMPLATE = "%sReader";
    private static final String WRITER_NAME_TEMPLATE = "%sWriter";

    public static String getReaderName(String className) {
        return String.format(READER_NAME_TEMPLATE, className);
    }

    public static String getWriterName(String className) {
        return String.format(WRITER_NAME_TEMPLATE, className);
    }

    private ScaleProcessorHelper() {
    }
}
