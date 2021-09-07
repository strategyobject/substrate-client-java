package com.strategyobject.substrateclient.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Header {
    private final BlockHash parentHash;
}
