package com.strategyobject.substrateclient.rpc;

import com.strategyobject.substrateclient.rpc.core.RpcGeneratedSectionFactory;
import com.strategyobject.substrateclient.rpc.core.RpcInterfaceInitializationException;
import com.strategyobject.substrateclient.rpc.sections.Author;
import com.strategyobject.substrateclient.rpc.sections.Chain;
import com.strategyobject.substrateclient.rpc.sections.State;
import com.strategyobject.substrateclient.rpc.sections.System;
import com.strategyobject.substrateclient.transport.ProviderInterface;
import lombok.NonNull;

public class RpcImpl implements Rpc, AutoCloseable {
    private final ProviderInterface providerInterface;
    private final Author author;
    private final Chain chain;
    private final State state;
    private final System system;

    private RpcImpl(ProviderInterface providerInterface) {
        this.providerInterface = providerInterface;
        author = resolveSection(Author.class);
        chain = resolveSection(Chain.class);
        state = resolveSection(State.class);
        system = resolveSection(System.class);
    }

    private <T> T resolveSection(Class<T> clazz) {
        try {
            return RpcGeneratedSectionFactory.create(clazz, providerInterface);
        } catch (RpcInterfaceInitializationException e) {
            throw new RuntimeException(e);
        }
    }

    public static RpcImpl with(@NonNull ProviderInterface providerInterface) {
        return new RpcImpl(providerInterface);
    }

    @Override
    public Author author() {
        return author;
    }

    @Override
    public Chain chain() {
        return chain;
    }

    @Override
    public State state() {
        return state;
    }

    @Override
    public System system() {
        return system;
    }

    @Override
    public void close() throws Exception {
        if (providerInterface instanceof AutoCloseable) {
            ((AutoCloseable) providerInterface).close();
        }
    }
}
