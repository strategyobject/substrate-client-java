package com.strategyobject.substrateclient.api.pallet.proxy;

import com.strategyobject.substrateclient.common.types.Result;
import com.strategyobject.substrateclient.common.types.Unit;
import com.strategyobject.substrateclient.pallet.annotation.Event;
import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.rpc.api.primitives.BlockNumber;
import com.strategyobject.substrateclient.rpc.api.primitives.CallHash;
import com.strategyobject.substrateclient.rpc.api.runtime.DispatchError;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

@Pallet("Proxy")
public interface Proxy {

    /**
     * A proxy was executed correctly, with the given \[result\].
     */
    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class ProxyExecuted {
        private Result<Unit, DispatchError> dispatchResult;
    }

    /**
     * Anonymous account has been created by new proxy with given
     * disambiguation index and proxy type. \[anonymous, who, proxy_type,
     * disambiguation_index\]
     */
    @Event(index = 1)
    @Getter
    @Setter
    @ScaleReader
    class AnonymousCreated {
        private AccountId anonymous;
        private AccountId who;
        private ProxyType proxyType;
        @Scale(ScaleType.U16.class)
        private Integer disambiguationIndex;
    }

    /**
     * An announcement was placed to make a call in the future. \[real, proxy, call_hash\]
     */
    @Event(index = 2)
    @Getter
    @Setter
    @ScaleReader
    class Announced {
        private AccountId real;
        private AccountId proxy;
        private CallHash callHash;
    }

    /**
     * A proxy was added. \[delegator, delegatee, proxy_type, delay\]
     */
    @Event(index = 3)
    @Getter
    @Setter
    @ScaleReader
    class ProxyAdded {
        private AccountId delegator;
        private AccountId delegatee;
        private ProxyType proxyType;
        private BlockNumber blockNumber;
    }
}
