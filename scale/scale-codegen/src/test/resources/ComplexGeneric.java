package com.strategyobject.substrateclient.scale;

import com.strategyobject.substrateclient.scale.ScaleType.Vec;
import com.strategyobject.substrateclient.scale.annotations.ScaleReader;
import com.strategyobject.substrateclient.scale.annotations.ScaleWriter;
import com.strategyobject.substrateclient.scale.annotations.*;
import com.strategyobject.substrateclient.types.Result;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@ScaleReader
@ScaleWriter
public class ComplexGeneric<T> {
    @ScaleGeneric(
            template = "Map<Vec<?>, Result<OptionBool, String>>",
            types = {
                    @Scale(Map.class),
                    @Scale(Vec.class),
                    @Scale(ScaleType.OptionBool.class),
                    @Scale(Result.class),
                    @Scale(String.class)
            }
    )
    private Map<List<T>, Result<Optional<Boolean>, String>> testGeneric;

    @ScaleGeneric(
            template = "Map<I32, Result>",
            types = {
                    @Scale(Map.class),
                    @Scale(ScaleType.I32.class),
                    @Scale(name = "Result"),
            }
    )
    private Map<Integer, Result<Boolean, String>> testGenericDefaultImplicit;

    @ScaleGeneric(
            template = "Map<I32, Result>",
            types = {
                    @Scale(Map.class),
                    @Scale(ScaleType.I32.class),
                    @Scale(value = Default.class, name = "Result"),
            }
    )
    private Map<Integer, Result<Boolean, String>> testGenericDefaultExplicit;

    public Map<List<T>, Result<Optional<Boolean>, String>> getTestGeneric() {
        return testGeneric;
    }

    public void setTestGeneric(Map<List<T>, Result<Optional<Boolean>, String>> testGeneric) {
        this.testGeneric = testGeneric;
    }

    public Map<Integer, Result<Boolean, String>> getTestGenericDefaultImplicit() {
        return testGenericDefaultImplicit;
    }

    public void setTestGenericDefaultImplicit(Map<Integer, Result<Boolean, String>> testGenericDefaultImplicit) {
        this.testGenericDefaultImplicit = testGenericDefaultImplicit;
    }

    public Map<Integer, Result<Boolean, String>> getTestGenericDefaultExplicit() {
        return testGenericDefaultExplicit;
    }

    public void setTestGenericDefaultExplicit(Map<Integer, Result<Boolean, String>> testGenericDefaultExplicit) {
        this.testGenericDefaultExplicit = testGenericDefaultExplicit;
    }
}
