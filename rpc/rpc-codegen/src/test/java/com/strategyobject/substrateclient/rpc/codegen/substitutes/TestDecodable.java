package com.strategyobject.substrateclient.rpc.codegen.substitutes;

import com.strategyobject.substrateclient.rpc.core.annotations.RpcDecoder;
import com.strategyobject.substrateclient.scale.annotations.Scale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@RpcDecoder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestDecodable<T> {
    private int a;
    private String b;
    private T c;
    private List<String> d;
    private Map<String, Integer> e;
    @Scale
    private int f;
    @Scale
    private List<Integer> g;
}
