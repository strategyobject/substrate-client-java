package com.strategyobject.substrateclient.common.eventemitter;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class EventEmitterTest {

    @Test
    void PermanentHandlerCalledMultiple() {
        val emitter = new EventEmitter();

        final Integer[] hitCount = {0};
        emitter.on(TestEvents.ONE, _x -> hitCount[0]++);

        emitter.emit(TestEvents.ONE);
        emitter.emit(TestEvents.ONE);

        assertEquals(2, hitCount[0]);
    }

    @Test
    void OneTimeHandlerCalledOnce() {
        val emitter = new EventEmitter();

        final Integer[] hitCount = {0};
        emitter.once(TestEvents.ONE, _x -> hitCount[0]++);

        emitter.emit(TestEvents.ONE);
        emitter.emit(TestEvents.ONE);

        assertEquals(1, hitCount[0]);
    }

    @Test
    void HandlerNotCalledOnDifferentEvent() {
        val emitter = new EventEmitter();

        final Integer[] hitCount = {0};
        emitter.on(TestEvents.ONE, _x -> hitCount[0]++);

        emitter.emit(TestEvents.TWO);

        assertEquals(0, hitCount[0]);
    }

    @Test
    void CanCallMultipleHandlers() {
        val emitter = new EventEmitter();

        final Integer[] hitCount = {0};
        emitter.once(TestEvents.ONE, _x -> hitCount[0]++)
                .on(TestEvents.ONE, _x -> hitCount[0]++)
                .on(TestEvents.TWO, _x -> hitCount[0]++);

        emitter.emit(TestEvents.ONE);

        assertEquals(2, hitCount[0]);
    }

    @Test
    void EmitReturnsFalseIfNoHandlers() {
        val emitter = new EventEmitter();

        final Integer[] hitCount = {0};
        emitter.on(TestEvents.TWO, _x -> hitCount[0]++);

        assertFalse(emitter.emit(TestEvents.ONE));
        assertEquals(0, hitCount[0]);
    }

    @Test
    void CanRemoveHandlers() {
        val emitter = new EventEmitter();

        final Integer[] hitCount = {0};
        EventListener handler = _x -> hitCount[0]++;
        emitter.on(TestEvents.ONE, handler)
                .once(TestEvents.ONE, _x -> hitCount[0] += 2)
                .removeListener(TestEvents.ONE, handler)
                .on(TestEvents.ONE, _x -> hitCount[0] += 3);

        emitter.emit(TestEvents.ONE);

        assertEquals(5, hitCount[0]);
    }
}

enum TestEvents implements EventType {
    ONE,
    TWO
}