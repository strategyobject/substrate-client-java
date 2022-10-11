package com.strategyobject.substrateclient.api.pallet.preimage;

import com.strategyobject.substrateclient.pallet.annotation.*;
import com.strategyobject.substrateclient.rpc.api.primitives.Hash;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

@Pallet("Preimage")
public interface Preimage {
    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class Noted {
        private Hash hash;
    }

    @Event(index = 1)
    @Getter
    @Setter
    @ScaleReader
    class Requested {
        private Hash hash;
    }

    @Event(index = 2)
    @Getter
    @Setter
    @ScaleReader
    class Cleared {
        private Hash hash;
    }
}
