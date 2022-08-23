package com.strategyobject.substrateclient.common.types.tuple;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "of")
@EqualsAndHashCode
public final class Pair<F, S> {
    private final F value0;
    private final S value1;
}
