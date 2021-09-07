package com.strategyobject.substrateclient.transport;

import com.strategyobject.substrateclient.common.eventemitter.EventType;

public enum ProviderInterfaceEmitted implements EventType {
    CONNECTED,
    DISCONNECTED,
    ERROR
}
