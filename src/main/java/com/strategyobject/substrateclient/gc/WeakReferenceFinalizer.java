package com.strategyobject.substrateclient.gc;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class WeakReferenceFinalizer<T> extends WeakReference<T> {
    private final Runnable finalizer;

    public WeakReferenceFinalizer(T referent, ReferenceQueue<? super T> q, Runnable finalizer) {
        super(referent, q);
        this.finalizer = finalizer;
    }

    public void finalizeResources() {
        finalizer.run();
    }
}
