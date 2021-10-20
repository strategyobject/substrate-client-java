package com.strategyobject.substrateclient.rpc.codegen.substitutes;

import com.strategyobject.substrateclient.rpc.core.annotations.RpcDecoder;
import lombok.Getter;
import lombok.Setter;

@RpcDecoder
@Getter
@Setter
public class TestDecodable<T> {
    public int a;
    public String b;
    public T c;

    public TestDecodable() {

    }

    public TestDecodable(int a, String b, T c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
}
