package com.strategyobject.substrateclient.scale;

import com.strategyobject.substrateclient.common.types.Result;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import com.strategyobject.substrateclient.scale.annotation.ScaleWriter;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@ScaleReader
@ScaleWriter
public class NonAnnotated<T1, T2, T3> {
    private Boolean testBool;

    private Byte testI8;

    private Short testI16;

    private Integer testI32;

    private Long testI64;

    private BigInteger testI128;

    private Optional<T1> testOption;

    private Result<T2, T3> testResult;

    private String testString;

    private List<T1> testVec;

    private T1 testGeneric1;

    private NestedClass<T2> testNestedField;

    public Boolean getTestBool() {
        return testBool;
    }

    public void setTestBool(Boolean testBool) {
        this.testBool = testBool;
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

    public Optional<T1> getTestOption() {
        return testOption;
    }

    public void setTestOption(Optional<T1> testOption) {
        this.testOption = testOption;
    }

    public Result<T2, T3> getTestResult() {
        return testResult;
    }

    public void setTestResult(Result<T2, T3> testResult) {
        this.testResult = testResult;
    }

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }

    public List<T1> getTestVec() {
        return testVec;
    }

    public void setTestVec(List<T1> testVec) {
        this.testVec = testVec;
    }

    public T1 getTestGeneric1() {
        return testGeneric1;
    }

    public void setTestGeneric1(T1 testGeneric1) {
        this.testGeneric1 = testGeneric1;
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
