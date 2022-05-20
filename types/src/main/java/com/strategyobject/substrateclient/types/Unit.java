package com.strategyobject.substrateclient.types;

public class Unit {
    private static final Unit UNIT = new Unit();

    public static Unit get() {
        return UNIT;
    }

    private Unit() {
    }
}
