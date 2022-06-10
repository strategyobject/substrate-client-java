package com.strategyobject.substrateclient.common.types;

public interface Size {
    int getValue();

    Zero zero = new Zero();
    Of32 of32 = new Of32();
    Of64 of64 = new Of64();
    Of96 of96 = new Of96();
    Of128 of128 = new Of128();
    Of256 of256 = new Of256();

    class Zero implements Size {
        @Override
        public int getValue() {
            return 0;
        }
    }

    class Of32 implements Size {
        @Override
        public int getValue() {
            return 32;
        }
    }

    class Of64 implements Size {
        @Override
        public int getValue() {
            return 64;
        }
    }

    class Of96 implements Size {
        @Override
        public int getValue() {
            return 96;
        }
    }

    class Of128 implements Size {
        @Override
        public int getValue() {
            return 128;
        }
    }

    class Of256 implements Size {
        @Override
        public int getValue() {
            return 256;
        }
    }
}
