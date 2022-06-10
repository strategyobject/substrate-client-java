package com.strategyobject.substrateclient.scale;

import com.strategyobject.substrateclient.common.types.Result;
import com.strategyobject.substrateclient.scale.ScaleType.*;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import com.strategyobject.substrateclient.scale.annotation.ScaleWriter;
import com.strategyobject.substrateclient.scale.annotation.*;

import java.lang.String;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@ScaleReader
@ScaleWriter
public class Annotated<T1, T2, T3> {
    @Scale(Bool.class)
    private Boolean testBool;

    @Scale(CompactBigInteger.class)
    private BigInteger testCompactBigInteger;

    @Scale(CompactInteger.class)
    private Integer testCompactInteger;

    @Scale(I8.class)
    private Byte testI8;

    @Scale(I16.class)
    private Short testI16;

    @Scale(I32.class)
    private Integer testI32;

    @Scale(I64.class)
    private Long testI64;

    @Scale(I128.class)
    private BigInteger testI128;

    @Scale(OptionBool.class)
    private Optional<Boolean> testOptionBool;

    @ScaleGeneric(
            template = "Option<I32>",
            types = {
                    @Scale(Option.class),
                    @Scale(I32.class)
            })
    private Optional<Integer> testOption;

    @ScaleGeneric(
            template = "Result<Bool, String>",
            types = {
                    @Scale(Result.class),
                    @Scale(Bool.class),
                    @Scale(ScaleType.String.class)
            }
    )
    private Result<Boolean, String> testResult;

    @Scale(ScaleType.String.class)
    private String testString;

    @Scale(U8.class)
    private Integer testU8;

    @Scale(U16.class)
    private Integer testU16;

    @Scale(U32.class)
    private Long testU32;

    @Scale(U64.class)
    private BigInteger testU64;

    @Scale(U128.class)
    private BigInteger testU128;

    @ScaleGeneric(
            template = "Union<I32, Bool, String>",
            types = {
                    @Scale(value = Union3.class, name = "Union"),
                    @Scale(I32.class),
                    @Scale(Bool.class),
                    @Scale(ScaleType.String.class)
            }
    )
    private com.strategyobject.substrateclient.common.types.union.Union3<Integer, Boolean, String> testUnion;

    @ScaleGeneric(
            template = "Vec<i32>",
            types = {
                    @Scale(value = I32.class, name = "i32"),
                    @Scale(Vec.class)
            }
    )
    private List<Integer> testVec;

    @Ignore
    private Object testIgnore;

    @Scale
    private T2 testGeneric1;

    @Scale
    private T3 testGeneric2;

    @Scale
    private NestedClass<T2> testNestedField;

    public Boolean getTestBool() {
        return testBool;
    }

    public void setTestBool(Boolean testBool) {
        this.testBool = testBool;
    }

    public BigInteger getTestCompactBigInteger() {
        return testCompactBigInteger;
    }

    public void setTestCompactBigInteger(BigInteger testCompactBigInteger) {
        this.testCompactBigInteger = testCompactBigInteger;
    }

    public Integer getTestCompactInteger() {
        return testCompactInteger;
    }

    public void setTestCompactInteger(Integer testCompactInteger) {
        this.testCompactInteger = testCompactInteger;
    }

    public Byte getTestI8() {
        return testI8;
    }

    public void setTestI8(Byte testI8) {
        this.testI8 = testI8;
    }

    public Short getTestI16() {
        return testI16;
    }

    public void setTestI16(Short testI16) {
        this.testI16 = testI16;
    }

    public Integer getTestI32() {
        return testI32;
    }

    public void setTestI32(Integer testI32) {
        this.testI32 = testI32;
    }

    public Long getTestI64() {
        return testI64;
    }

    public void setTestI64(Long testI64) {
        this.testI64 = testI64;
    }

    public BigInteger getTestI128() {
        return testI128;
    }

    public void setTestI128(BigInteger testI128) {
        this.testI128 = testI128;
    }

    public Optional<Boolean> getTestOptionBool() {
        return testOptionBool;
    }

    public void setTestOptionBool(Optional<Boolean> testOptionBool) {
        this.testOptionBool = testOptionBool;
    }

    public Optional<Integer> getTestOption() {
        return testOption;
    }

    public void setTestOption(Optional<Integer> testOption) {
        this.testOption = testOption;
    }

    public Result<Boolean, String> getTestResult() {
        return testResult;
    }

    public void setTestResult(Result<Boolean, String> testResult) {
        this.testResult = testResult;
    }

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }

    public Integer getTestU8() {
        return testU8;
    }

    public void setTestU8(Integer testU8) {
        this.testU8 = testU8;
    }

    public Integer getTestU16() {
        return testU16;
    }

    public void setTestU16(Integer testU16) {
        this.testU16 = testU16;
    }

    public Long getTestU32() {
        return testU32;
    }

    public void setTestU32(Long testU32) {
        this.testU32 = testU32;
    }

    public BigInteger getTestU64() {
        return testU64;
    }

    public void setTestU64(BigInteger testU64) {
        this.testU64 = testU64;
    }

    public BigInteger getTestU128() {
        return testU128;
    }

    public void setTestU128(BigInteger testU128) {
        this.testU128 = testU128;
    }

    public com.strategyobject.substrateclient.common.types.union.Union3<Integer, Boolean, String> getTestUnion() {
        return testUnion;
    }

    public void setTestUnion(com.strategyobject.substrateclient.common.types.union.Union3<Integer, Boolean, String> testUnion) {
        this.testUnion = testUnion;
    }

    public List<Integer> getTestVec() {
        return testVec;
    }

    public void setTestVec(List<Integer> testVec) {
        this.testVec = testVec;
    }

    public Object getTestIgnore() {
        return testIgnore;
    }

    public void setTestIgnore(Object testIgnore) {
        this.testIgnore = testIgnore;
    }

    public T2 getTestGeneric1() {
        return testGeneric1;
    }

    public void setTestGeneric1(T2 testGeneric1) {
        this.testGeneric1 = testGeneric1;
    }

    public T3 getTestGeneric2() {
        return testGeneric2;
    }

    public void setTestGeneric2(T3 testGeneric2) {
        this.testGeneric2 = testGeneric2;
    }

    public NestedClass<T2> getTestNestedField() {
        return testNestedField;
    }

    public void setTestNestedField(NestedClass<T2> testNestedField) {
        this.testNestedField = testNestedField;
    }

    public static class NestedClass<T> {
        public T nestedField;
    }
}
