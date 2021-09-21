package com.strategyobject.substrateclient.scale;

public interface ScaleType {
    class Bool implements ScaleType {
    }

    class CompactBigInteger implements ScaleType {
    }

    class CompactInteger implements ScaleType {
    }

    class I8 implements ScaleType {
    }

    class I16 implements ScaleType {
    }

    class I32 implements ScaleType {
    }

    class I64 implements ScaleType {
    }

    class I128 implements ScaleType {
    }

    class OptionBool implements ScaleType {
    }

    class Option<T> implements ScaleType {
    }

    class Result<T, E> implements ScaleType {
    }

    class String implements ScaleType {
    }

    class U8 implements ScaleType {
    }

    class U16 implements ScaleType {
    }

    class U32 implements ScaleType {
    }

    class U64 implements ScaleType {
    }

    class U128 implements ScaleType {
    }

    class Vec<T> implements ScaleType {
    }

    class Union implements ScaleType {
    }
}
