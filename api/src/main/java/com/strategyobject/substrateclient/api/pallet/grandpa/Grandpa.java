package com.strategyobject.substrateclient.api.pallet.grandpa;

import com.strategyobject.substrateclient.pallet.annotation.Event;
import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Pallet("Grandpa")
public interface Grandpa {

    /**
     * New authority set has been applied. \[authority_set\]
     */
    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class NewAuthorities {
        private List<Authority> authorityList;
    }

    /**
     * Current authority set has been paused.
     */
    @Event(index = 1)
    @ScaleReader
    class Paused {
    }

    /**
     * Current authority set has been resumed.
     */
    @Event(index = 2)
    @ScaleReader
    class Resumed {
    }

}
