package com.strategyobject.substrateclient.rpc.provider;

import com.strategyobject.substrateclient.eventemitter.EventType;

public enum ProviderInterfaceEmitted implements EventType {
    CONNECTED,
    DISCONNECTED,
    ERROR
}
