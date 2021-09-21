package com.strategyobject.substrateclient.scale;

import com.strategyobject.substrateclient.scale.annotations.ScaleReader;
import com.strategyobject.substrateclient.scale.annotations.ScaleWriter;
import com.strategyobject.substrateclient.types.Result;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@ScaleReader
@ScaleWriter
public class NonAnnotated<T1, T2, T3> {
    public Boolean testBool;

    public Byte testI8;

    public Short testI16;

    public Integer testI32;

    public Long testI64;

    public BigInteger testI128;

    public Optional<T1> testOption;

    public Result<T2, T3> testResult;

    public String testString;

    public List<T1> testVec;

    public T1 testGeneric1;

    public NestedClass<T2> testNestedField;

    public static class NestedClass<T> {
        public T nestedField;
    }
}
