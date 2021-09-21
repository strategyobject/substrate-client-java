package com.strategyobject.substrateclient.scale;

import com.strategyobject.substrateclient.scale.ScaleType.*;
import com.strategyobject.substrateclient.scale.annotations.ScaleReader;
import com.strategyobject.substrateclient.scale.annotations.ScaleWriter;
import com.strategyobject.substrateclient.scale.annotations.*;
import com.strategyobject.substrateclient.types.Result;

import java.lang.String;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@ScaleReader
@ScaleWriter
public class Annotated<T1, T2, T3> {
    @Scale(Bool.class)
    public Boolean testBool;

    @Scale(CompactBigInteger.class)
    public BigInteger testCompactBigInteger;

    @Scale(CompactInteger.class)
    public Integer testCompactInteger;

    @Scale(I8.class)
    public Byte testI8;

    @Scale(I16.class)
    public Short testI16;

    @Scale(I32.class)
    public Integer testI32;

    @Scale(I64.class)
    public Long testI64;

    @Scale(I128.class)
    public BigInteger testI128;

    @Scale(OptionBool.class)
    public Optional<Boolean> testOptionBool;


    @ScaleGeneric(
            template = "Option<I32>",
            types = {
                    @Scale(Option.class),
                    @Scale(I32.class)
            })
    public Optional<Integer> testOption;

    @ScaleGeneric(
            template = "Result<Bool, String>",
            types = {
                    @Scale(Result.class),
                    @Scale(Bool.class),
                    @Scale(ScaleType.String.class)
            }
    )
    public Result<Boolean, String> testResult;

    @Scale(ScaleType.String.class)
    public String testString;

    @Scale(U8.class)
    public Integer testU8;

    @Scale(U16.class)
    public Integer testU16;

    @Scale(U32.class)
    public Long testU32;

    @Scale(U64.class)
    public BigInteger testU64;

    @Scale(U128.class)
    public BigInteger testU128;

    @ScaleGeneric(
            template = "Vec<i32>",
            types = {
                    @Scale(value = I32.class, name = "i32"),
                    @Scale(Vec.class)
            }
    )
    public List<Integer> testVec;

    @Ignore
    public Object testIgnore;

    @Scale
    public T2 testGeneric1;

    @Scale
    public T3 testGeneric2;

    @Scale
    public NestedClass<T2> testNestedField;

    public static class NestedClass<T> {
        public T nestedField;
    }
}
