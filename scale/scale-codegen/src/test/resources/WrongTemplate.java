package com.strategyobject.substrateclient.scale;

import com.strategyobject.substrateclient.common.types.Result;
import com.strategyobject.substrateclient.scale.ScaleType.Vec;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleGeneric;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import com.strategyobject.substrateclient.scale.annotation.ScaleWriter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@ScaleReader
@ScaleWriter
public class WrongTemplate<T> {
    @ScaleGeneric(
            template = "Map<Vec<?>, Result<OptionBool, String>",
            types = {
                    @Scale(Map.class),
                    @Scale(Vec.class),
                    @Scale(ScaleType.OptionBool.class),
                    @Scale(Result.class),
                    @Scale(String.class)
            }
    )
    private Map<List<T>, Result<Optional<Boolean>, String>> testGeneric;

    public Map<List<T>, Result<Optional<Boolean>, String>> getTestGeneric() {
        return testGeneric;
    }

    public void setTestGeneric(Map<List<T>, Result<Optional<Boolean>, String>> testGeneric) {
        this.testGeneric = testGeneric;
    }
}
