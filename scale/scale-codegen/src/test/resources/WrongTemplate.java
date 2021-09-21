package com.strategyobject.substrateclient.scale;

import com.strategyobject.substrateclient.scale.ScaleType.Vec;
import com.strategyobject.substrateclient.scale.annotations.Scale;
import com.strategyobject.substrateclient.scale.annotations.ScaleGeneric;
import com.strategyobject.substrateclient.scale.annotations.ScaleReader;
import com.strategyobject.substrateclient.scale.annotations.ScaleWriter;
import com.strategyobject.substrateclient.types.Result;

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
    public Map<List<T>, Result<Optional<Boolean>, String>> testGeneric;
}
