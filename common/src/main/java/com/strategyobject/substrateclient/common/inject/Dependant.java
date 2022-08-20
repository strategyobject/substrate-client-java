package com.strategyobject.substrateclient.common.inject;

public interface Dependant<R, D> {
    @SuppressWarnings("unchecked")
    R inject(D... dependencies);
}
