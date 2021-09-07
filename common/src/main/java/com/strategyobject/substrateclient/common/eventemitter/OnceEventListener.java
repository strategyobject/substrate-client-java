package com.strategyobject.substrateclient.common.eventemitter;

import java.util.concurrent.atomic.AtomicBoolean;

class OnceEventListener implements EventListener {
    private final AtomicBoolean isExecuted = new AtomicBoolean();
    private final EventListener listener;

    public OnceEventListener(EventListener listener) {
        this.listener = listener;
    }

    @Override
    public void onEvent(Object... args) {
        if (isExecuted.compareAndSet(false, true)) {
            this.listener.onEvent(args);
        }
    }

    @Override
    public boolean isOnce() {
        return true;
    }
}
