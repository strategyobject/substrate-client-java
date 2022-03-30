package com.strategyobject.substrateclient.rpc;

import com.strategyobject.substrateclient.rpc.sections.Author;
import com.strategyobject.substrateclient.rpc.sections.Chain;
import com.strategyobject.substrateclient.rpc.sections.State;
import com.strategyobject.substrateclient.rpc.sections.System;

public interface Rpc {
    Author author();

    Chain chain();

    State state();

    System system();
}
