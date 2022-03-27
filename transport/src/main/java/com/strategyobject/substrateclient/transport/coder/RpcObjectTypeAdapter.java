package com.strategyobject.substrateclient.transport.coder;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.strategyobject.substrateclient.transport.*;
import lombok.val;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RpcObjectTypeAdapter extends TypeAdapter<RpcObject> {
    @Override
    public void write(JsonWriter out, RpcObject value) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public RpcObject read(JsonReader in) throws IOException {
        switch (in.peek()) {
            case NULL:
                in.nextNull();
                return new RpcNull();
            case BOOLEAN:
                return new RpcBoolean(in.nextBoolean());
            case NUMBER:
                return new RpcNumber(in.nextDouble());
            case STRING:
                return new RpcString(in.nextString());
            case BEGIN_ARRAY:
                in.beginArray();
                val list = new ArrayList<RpcObject>();
                while (in.hasNext()) {
                    list.add(this.read(in));
                }

                in.endArray();
                return new RpcList(list);
            default:
                in.beginObject();
                val map = new HashMap<String, RpcObject>();
                while (in.hasNext()) {
                    map.put(in.nextName(), this.read(in));
                }

                in.endObject();
                return new RpcMap(map);
        }
    }
}
