package com.strategyobject.substrateclient.rpc;

import com.strategyobject.substrateclient.rpc.sections.Author;
import com.strategyobject.substrateclient.rpc.sections.Chain;
import com.strategyobject.substrateclient.rpc.sections.State;

public interface Rpc {
    Author getAuthor();

    Chain getChain();

    State getState();

    System getSystem();
}
