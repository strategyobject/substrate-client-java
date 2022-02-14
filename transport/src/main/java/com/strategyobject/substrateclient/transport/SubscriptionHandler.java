package com.strategyobject.substrateclient.transport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.function.BiConsumer;

@AllArgsConstructor
@Getter
@Setter
public class SubscriptionHandler {
    BiConsumer<Exception, RpcObject> callBack;
    String type;
}
