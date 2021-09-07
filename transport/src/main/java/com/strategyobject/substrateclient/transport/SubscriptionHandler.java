package com.strategyobject.substrateclient.transport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;
import java.util.function.BiConsumer;

@AllArgsConstructor
@Getter
@Setter
public class SubscriptionHandler<T> {
    BiConsumer<Exception, T> callBack;
    String type;
    Type resultType;
}
