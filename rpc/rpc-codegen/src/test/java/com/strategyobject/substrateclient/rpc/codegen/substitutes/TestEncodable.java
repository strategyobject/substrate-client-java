package com.strategyobject.substrateclient.rpc.codegen.substitutes;

import com.strategyobject.substrateclient.rpc.core.annotations.RpcEncoder;
import com.strategyobject.substrateclient.scale.annotations.Scale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@RpcEncoder
@Getter
@Setter
@AllArgsConstructor
public class TestEncodable<T> {
    @Scale
    private int a;
    private String b;
    private T c;
    private boolean d;
    private List<String> e;
    private Map<String, Integer> f;
    @Scale
    private List<Integer> h;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @RpcEncoder
    public static class Subclass<T> {
        public T a;
    }
}
