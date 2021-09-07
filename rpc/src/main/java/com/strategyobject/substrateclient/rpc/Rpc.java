package com.strategyobject.substrateclient.rpc;

import com.strategyobject.substrateclient.rpc.sections.Author;
import com.strategyobject.substrateclient.rpc.sections.State;

public interface Rpc {
    Author getAuthor();

    State getState();
}
