package com.strategyobject.substrateclient.rpc;

import com.strategyobject.substrateclient.rpc.core.RpcGeneratedSectionFactory;
import com.strategyobject.substrateclient.rpc.core.RpcInterfaceInitializationException;
import com.strategyobject.substrateclient.rpc.sections.Author;
import com.strategyobject.substrateclient.rpc.sections.Chain;
import com.strategyobject.substrateclient.rpc.sections.State;
import com.strategyobject.substrateclient.rpc.sections.System;
import com.strategyobject.substrateclient.transport.ProviderInterface;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RpcImpl implements Rpc, AutoCloseable {
    private final ProviderInterface providerInterface;

    @Override
    public Author author() {
        try {
            return RpcGeneratedSectionFactory.create(Author.class, providerInterface);
        } catch (RpcInterfaceInitializationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Chain chain() {
        try {
            return RpcGeneratedSectionFactory.create(Chain.class, providerInterface);
        } catch (RpcInterfaceInitializationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public State state() {
        try {
            return RpcGeneratedSectionFactory.create(State.class, providerInterface);
        } catch (RpcInterfaceInitializationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public System system() {
        try {
            return RpcGeneratedSectionFactory.create(System.class, providerInterface);
        } catch (RpcInterfaceInitializationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        if (providerInterface instanceof AutoCloseable) {
            ((AutoCloseable) providerInterface).close();
        }
    }
}
