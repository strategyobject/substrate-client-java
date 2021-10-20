package com.strategyobject.substrateclient.rpc.codegen.substitutes;

import com.strategyobject.substrateclient.rpc.core.annotations.RpcEncoder;
import com.strategyobject.substrateclient.scale.annotations.Scale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RpcEncoder
@Getter
@Setter
public class TestEncodable<T> {
    @Scale
    public int a;
    public String b;
    public T c;
    public boolean d;

    public TestEncodable() {

    }

    public TestEncodable(int a, String b, T c, boolean d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @RpcEncoder
    public static class Subclass<T> {
        public T a;
    }
}
