package com.strategyobject.substrateclient.api.pallet.session;

import com.strategyobject.substrateclient.pallet.annotation.Event;
import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

@Pallet("Session")
public interface Session {

    /**
     * New session has happened. Note that the argument is the \[session_index\], not the
     * block number as the type might suggest.
     */
    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class NewSession {
        @Scale(ScaleType.U32.class)
        private Long sessionIndex;
    }

}
