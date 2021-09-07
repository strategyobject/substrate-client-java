package com.strategyobject.substrateclient.rpc.codegen.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface RpcSubscription {
    String type();
    String subscribeMethod();
    String unsubscribeMethod();
}
