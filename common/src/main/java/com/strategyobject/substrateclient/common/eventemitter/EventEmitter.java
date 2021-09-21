package com.strategyobject.substrateclient.common.eventemitter;

import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventEmitter {
    final Map<EventType, List<EventListener>> listeners = new ConcurrentHashMap<>();

    public boolean emit(EventType eventType, Object... args) {
        val eventListeners = listeners.get(eventType);
        if (eventListeners == null || eventListeners.isEmpty()) {
            return false;
        }

        val disposableListeners = new ArrayList<EventListener>();
        for (EventListener eventListener : eventListeners) {
            eventListener.onEvent(args);

            if (eventListener.isOnce()) {
                disposableListeners.add(eventListener);
            }
        }

        eventListeners.removeAll(disposableListeners);
        return true;
    }

    public EventEmitter on(EventType eventType, EventListener listener) {
        val eventListeners = this.listeners.computeIfAbsent(eventType, _x -> new CopyOnWriteArrayList<>());
        eventListeners.add(listener);
        return this;
    }

    public EventEmitter once(EventType eventType, EventListener listener) {
        val eventListeners = this.listeners.computeIfAbsent(eventType, _x -> new CopyOnWriteArrayList<>());
        eventListeners.add(new OnceEventListener(listener));
        return this;
    }

    public EventEmitter removeListener(EventType event, EventListener listener) {
        val eventListeners = this.listeners.get(event);
        if (eventListeners != null) {
            eventListeners.remove(listener);
        }

        return this;
    }
}
