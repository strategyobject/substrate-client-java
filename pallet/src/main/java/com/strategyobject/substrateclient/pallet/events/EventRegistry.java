package com.strategyobject.substrateclient.pallet.events;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.strategyobject.substrateclient.common.reflection.Scanner;
import com.strategyobject.substrateclient.pallet.annotation.Event;
import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class EventRegistry {
    private final Map<String, List<Class<?>>> map = new ConcurrentHashMap<>(32);

    private Collection<Class<?>> expand(Class<?> event, int offsetSize) {
        return Lists.newArrayList(
                Iterables.concat(
                        Collections.nCopies(offsetSize, null),
                        Collections.singleton(event)));
    }

    public void registerAnnotatedFrom(String... prefixes) {

        Scanner.forPrefixes(prefixes)
                .getTypesAnnotatedWith(Event.class)
                .stream()
                .sorted(Comparator.<Class<?>>comparingInt(x -> x.getAnnotation(Event.class).index()).reversed())
                .forEach(event -> {
                    val annotation = event.getAnnotation(Event.class);

                    try {
                        val eventIndex = annotation.index();
                        var palletName = annotation.pallet();
                        if (Strings.isNullOrEmpty(palletName)) {
                            val pallet = event.getDeclaringClass();
                            val palletAnnotation = pallet.getAnnotation(Pallet.class);
                            palletName = palletAnnotation.value();
                        }

                        log.info("Auto register event {} with index {} from {}",
                                event.getSimpleName(),
                                eventIndex,
                                palletName);

                        register(palletName, eventIndex, event);
                    } catch (Exception e) {
                        log.error("Auto registration error", e);
                    }
                });
    }

    public <T> void register(@NonNull String pallet, int eventIndex, @NonNull Class<T> event) {
        val events = map.get(pallet);
        if (events == null) {
            map.put(pallet, new CopyOnWriteArrayList<>(expand(event, eventIndex)));
        } else if (events.size() <= eventIndex) {
            events.addAll(expand(event, eventIndex - events.size()));
        } else {
            events.set(eventIndex, event);
        }
    }

    public Class<?> resolve(@NonNull String pallet, int eventIndex) {
        val events = map.get(pallet);
        if (events == null) {
            return null;
        }

        return events.get(eventIndex);
    }
}
