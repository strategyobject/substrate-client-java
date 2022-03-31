package com.strategyobject.substrateclient.transport.coder;

import com.strategyobject.substrateclient.transport.RpcObject;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

class RpcCoderTest {

    @Test
    void decodeNullResult() {
        val json = "{\n" +
                "    \"result\": null,\n" +
                "    \"id\": 0,\n" +
                "    \"jsonrpc\": \"3.0\"\n" +
                "}";
        val actual = RpcCoder.decodeJson(json);

        val expected = new JsonRpcResponse();
        expected.jsonrpc = "3.0";
        expected.id = 0;
        expected.result = RpcObject.ofNull();
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void decodeJson() {
        val json = "{\n" +
                "    \"result\": {\n" +
                "        \"null\": null,\n" +
                "        \"bool\": true,\n" +
                "        \"list\": [\n" +
                "            true,\n" +
                "            null,\n" +
                "            false\n" +
                "        ],\n" +
                "        \"map\": {\n" +
                "            \"null\": null,\n" +
                "            \"bool\": false,\n" +
                "            \"list\": [],\n" +
                "            \"map\": {},\n" +
                "            \"num\": -1,\n" +
                "            \"str\": \"\"\n" +
                "        },\n" +
                "        \"num\": 123.456,\n" +
                "        \"str\": \"string\"\n" +
                "    },\n" +
                "    \"id\": 10,\n" +
                "    \"jsonrpc\": \"3.0\"\n" +
                "}";
        val actual = RpcCoder.decodeJson(json);

        val expected = new JsonRpcResponse();
        expected.jsonrpc = "3.0";
        expected.id = 10;
        expected.result = RpcObject.of(new HashMap<String, RpcObject>() {{
            put("null", RpcObject.ofNull());
            put("bool", RpcObject.of(true));
            put("list", RpcObject.of(Arrays.asList(RpcObject.of(true), RpcObject.ofNull(), RpcObject.of(false))));
            put("map", RpcObject.of(new HashMap<String, RpcObject>() {{
                put("null", RpcObject.ofNull());
                put("bool", RpcObject.of(true));
                put("list", RpcObject.of(Collections.emptyList()));
                put("num", RpcObject.of(-1));
                put("map", RpcObject.of(new HashMap<>()));
                put("str", RpcObject.of(""));
            }}));
            put("num", RpcObject.of(123.456));
            put("str", RpcObject.of("string"));
        }});
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

}