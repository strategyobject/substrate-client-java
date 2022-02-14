package com.strategyobject.substrateclient.transport.coder;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.strategyobject.substrateclient.transport.*;
import lombok.val;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class RpcObjectDeserializer implements JsonDeserializer<RpcObject> {
    @Override
    public RpcObject deserialize(JsonElement json,
                                 Type typeOfT,
                                 JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonNull()) {
            return new RpcNull();
        }

        if (json.isJsonPrimitive()) {
            val primitive = json.getAsJsonPrimitive();

            if (primitive.isBoolean()) {
                return new RpcBoolean(primitive.getAsBoolean());
            }

            if (primitive.isNumber()) {
                return new RpcNumber(primitive.getAsNumber());
            }

            return new RpcString(primitive.getAsString());
        }

        if (json.isJsonArray()) {
            val jsonArray = json.getAsJsonArray();
            val list = new ArrayList<RpcObject>(jsonArray.size());
            for (val item : jsonArray) {
                list.add(context.deserialize(item, RpcObject.class));
            }

            return new RpcList(list);
        }

        val jsonObject = json.getAsJsonObject();
        val map = new HashMap<String, RpcObject>(jsonObject.size());
        for (val item : jsonObject.entrySet()) {
            map.put(item.getKey(), context.deserialize(item.getValue(), RpcObject.class));
        }

        return new RpcMap(map);
    }
}
